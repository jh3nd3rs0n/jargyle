package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Relay;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Socks5ValueDerivationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.util.Iterator;

final class BindRequestWorker extends TcpBasedRequestWorker {
	
	private final Logger logger;
		
	public BindRequestWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubNegotiationResults methSubNegotiationResults,
			final Request req) {
		super(socks5Worker, methSubNegotiationResults, req);
		this.logger = LoggerFactory.getLogger(BindRequestWorker.class);
	}
	
	private Socket acceptInboundSocketFrom(final ServerSocket listenSocket) {
		Socket inboundSocket = null;
		try {
			inboundSocket = listenSocket.accept();
		} catch (IOException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in waiting for an inbound socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return inboundSocket;
	}
	
	private boolean canAllowSecondReply() {
		Rule applicableRule = this.getApplicableRule();
		RuleContext ruleContext = this.getRuleContext();
		if (!this.hasSecondReplyRuleCondition()) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
			return false;
		}
		LogAction firewallActionLogAction = 
				applicableRule.getLastRuleActionValue(
						GeneralRuleActionSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowSecondReplyWithinLimit()) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Second SOCKS5 reply allowed based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								applicableRule,
								ruleContext));					
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"Second SOCKS5 reply denied based on the "
							+ "following rule and context: rule: %s "
							+ "context: %s",
							applicableRule,
							ruleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		this.sendReply(
				Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
		return false;
	}
	
	private boolean canAllowSecondReplyWithinLimit() {
		Rule applicableRule = this.getApplicableRule();
		RuleContext ruleContext = this.getRuleContext();
		NonNegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleActionValue(
						GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleActionValue(
						GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (!firewallActionAllowLimit.tryIncrementCurrentCount()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							ObjectLogMessageHelper.objectLogMessage(
									this,
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									applicableRule,
									ruleContext));
				}
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return false;				
			}
			this.addBelowAllowLimitRule(applicableRule);
		}
		return true;
	}
	
	private boolean configureInboundSocket(final Socket inboundSocket) {
		SocketSettings socketSettings = this.getInboundSocketSettings();
		try {
			socketSettings.applyTo(inboundSocket);
		} catch (UnsupportedOperationException | SocketException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the inbound socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private boolean configureListenSocket(final ServerSocket listenSocket) {
		SocketSettings socketSettings = this.getListenSocketSettings();
		try {
			socketSettings.applyTo(listenSocket);
		} catch (UnsupportedOperationException | SocketException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the listen socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}

	private SocketSettings getInboundSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(
				this.getApplicableRule(), this.getSettings());
	}

	private Host getListenBindHost() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(
				this.getApplicableRule(), this.getSettings());
	}
	
	private PortRanges getListenBindPortRanges() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(
				this.getApplicableRule(), this.getSettings());
	}
	
	private SocketSettings getListenSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(
				this.getApplicableRule(), this.getSettings());
	}
	
	private int getRelayBufferSize() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(
				this.getApplicableRule(), this.getSettings()).intValue();
	}
	
	private int getRelayIdleTimeout() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(
				this.getApplicableRule(), this.getSettings()).intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(
						this.getApplicableRule(), this.getSettings());
		if (relayInboundBandwidthLimit != null) {
			return relayInboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(
						this.getApplicableRule(), this.getSettings());
		if (relayOutboundBandwidthLimit != null) {
			return relayOutboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private boolean hasSecondReplyRuleCondition() {
		Rule applicableRule = this.getApplicableRule();
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_ADDRESS)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_PORT)) {
			return true;
		}
		return false;
	}

	private Socket limitClientSocket(final Socket clientSocket) {
		Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
		if (outboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					clientSocket, outboundBandwidthLimit.intValue());
		}
		return clientSocket;
	}
	
	private Socket limitInboundSocket(final Socket inboundSocket) {
		Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
		if (inboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					inboundSocket, inboundBandwidthLimit.intValue());
		}
		return inboundSocket;
	}
	
	private ServerSocket newListenSocket() {
		InetAddress desiredDestinationInetAddress =
				this.resolveDesiredDestinationAddress(
						this.getDesiredDestinationAddress().toString());
		if (desiredDestinationInetAddress == null) {
			return null;
		}
		InetAddress bindInetAddress = desiredDestinationInetAddress;
		if (HostAddress.isAllZerosHostAddress(
				desiredDestinationInetAddress.getHostAddress())) {
			Host listenBindHost = this.getListenBindHost();
			try {
				bindInetAddress = listenBindHost.toInetAddress();
			} catch (UnknownHostException e) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Unable to bind the listen socket to the "
								+ "following host: %s",
								listenBindHost));
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
				return null;
			}
		}
		int desiredDestinationPort = this.getDesiredDestinationPort().intValue();
		PortRanges bindPortRanges = (desiredDestinationPort == 0) ?
				this.getListenBindPortRanges() : PortRanges.of(
						PortRange.of(Port.valueOf(
								desiredDestinationPort)));
		ServerSocket listenSocket = null;
		boolean listenSocketBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!listenSocketBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!listenSocketBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					listenSocket = this.newListenSocket(
							bindInetAddress, bindPort);
				} catch (BindException e) {
					continue;
				}
				if (listenSocket == null) {
					return null;
				}
				listenSocketBound = true;
			}
		}
		if (!listenSocketBound) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the listen socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return listenSocket;
	}
	
	private ServerSocket newListenSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		NetObjectFactory netObjectFactory =
				this.getSelectedRoute().getNetObjectFactory();
		ServerSocket listenSocket = null;
		try {
			listenSocket = netObjectFactory.newServerSocket();
		} catch (IOException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the listen socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		if (!this.configureListenSocket(listenSocket)) {
			try {
				listenSocket.close();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			return null;
		}
		try {
			listenSocket.bind(new InetSocketAddress(
					bindInetAddress, bindPort.intValue()));
		} catch (BindException e) {
			try {
				listenSocket.close();
			} catch (IOException ex) {
				throw new AssertionError(ex);
			}
			throw e;
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(e, BindException.class)) {
				try {
					listenSocket.close();
				} catch (IOException ex) {
					throw new AssertionError(ex);
				}
				throw new BindException();
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the listen socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			try {
				listenSocket.close();
			} catch (IOException ex) {
				throw new AssertionError(ex);
			}
			return null;
		}
		return listenSocket;
	}
	
	private RuleContext newSecondReplyRuleContext(
			final Reply secondRep) {
		RuleContext secondReplyRuleContext = new RuleContext(
				this.getRuleContext());
		secondReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_ADDRESS,
				secondRep.getServerBoundAddress().toString());
		secondReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_PORT,
				secondRep.getServerBoundPort());		
		return secondReplyRuleContext;
	}
	
	private InetAddress resolveDesiredDestinationAddress(
			final String desiredDestinationAddress) {
		NetObjectFactory netObjectFactory =
				this.getSelectedRoute().getNetObjectFactory();
		HostResolver hostResolver =	netObjectFactory.newHostResolver();
		InetAddress desiredDestinationInetAddress = null;
		try {
			desiredDestinationInetAddress = hostResolver.resolve(
					desiredDestinationAddress);
		} catch (UnknownHostException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to resolve the desired destination "
							+ "address for the listen socket: %s",
							desiredDestinationAddress), 
					e);
			this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(
					e, UnknownHostException.class)) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Unable to resolve the desired destination "
								+ "address for the listen socket: %s",
								desiredDestinationAddress), 
						e);
				this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
				return null;				
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in resolving the desired destination "
							+ "address for the listen socket: %s",
							desiredDestinationAddress), 
					e);
			this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		}
		return desiredDestinationInetAddress;
	}
	
	@Override
	public void run() {
		ServerSocket listenSocket = null;
		Reply rep = null;
		Socket inboundSocket = null;
		Socket clientSocket = this.getClientSocket();
		Reply secondRep = null;
		try {
			listenSocket = this.newListenSocket();
			if (listenSocket == null) {
				return;
			}
			InetAddress inetAddress = listenSocket.getInetAddress();
			String serverBoundAddress =	inetAddress.getHostAddress();
			int serverBoundPort = listenSocket.getLocalPort();
			rep = Reply.newSuccessInstance(
					Address.newInstanceFrom(serverBoundAddress),
					Port.valueOf(serverBoundPort));
			RuleContext ruleContext = this.newReplyRuleContext(rep);
			this.setRuleContext(ruleContext);
			Rule applicableRule = this.getRules().firstAppliesTo(
					this.getRuleContext());
			if (applicableRule == null) {
				this.logger.warn(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"No applicable rule found based on the following "
						+ "context: %s",
						this.getRuleContext()));				
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return;
			}			
			this.setApplicableRule(applicableRule);
			if (!this.canAllowReply()) {
				return;
			}
			if (!this.sendReply(rep)) {
				return;
			}
			inboundSocket = this.acceptInboundSocketFrom(listenSocket);
			try {
				listenSocket.close();
			} catch (IOException e) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in closing the listen socket"), 
						e);
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
				return;
			}
			if (inboundSocket == null) {
				return;
			}
			if (!this.configureInboundSocket(inboundSocket)) {
				return;
			}
			serverBoundAddress = 
					inboundSocket.getInetAddress().getHostAddress();
			serverBoundPort = inboundSocket.getPort();
			secondRep = Reply.newSuccessInstance(
					Address.newInstanceFrom(serverBoundAddress),
					Port.valueOf(serverBoundPort));
			ruleContext = this.newSecondReplyRuleContext(
					secondRep);
			this.setRuleContext(ruleContext);
			applicableRule = this.getRules().firstAppliesTo(
					this.getRuleContext());
			if (applicableRule == null) {
				this.logger.warn(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"No applicable rule found based on the following "
						+ "context: %s",
						this.getRuleContext()));				
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return;
			}			
			this.setApplicableRule(applicableRule);
			if (!this.canAllowSecondReply()) {
				return;
			}
			if (!this.sendReply(secondRep)) {
				return;
			}
			inboundSocket = this.limitInboundSocket(inboundSocket);
			clientSocket = this.limitClientSocket(clientSocket);
			Relay.Builder builder = new Relay.Builder(
					clientSocket, inboundSocket);
			builder.bufferSize(this.getRelayBufferSize());
			builder.idleTimeout(this.getRelayIdleTimeout());
			Relay relay = builder.build();
			try {
				this.passData(relay);
			} catch (IOException e) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in starting to pass data"), 
						e);				
			}
		} finally {
			if (inboundSocket != null && !inboundSocket.isClosed()) {
				try {
					inboundSocket.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
			if (listenSocket != null && !listenSocket.isClosed()) {
				try {
					listenSocket.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
	}

}
