package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Relay;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

final class BindRequestHandler extends RequestHandler {

    private final RelayRequestHandlerContext context;
	private final ServerEventLogger serverEventLogger;
		
	public BindRequestHandler(final RelayRequestHandlerContext cntxt) {
        this.context = cntxt;
        this.context.setLogMessageSource(this);
		this.serverEventLogger = cntxt.getServerEventLogger();
	}
	
	private Socket acceptInboundSocketFrom(final ServerSocket listenSocket) {
		Socket inboundSocket;
		try {
			inboundSocket = listenSocket.accept();
		} catch (IOException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in waiting for an inbound socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return inboundSocket;
	}
	
	private boolean canAllowSecondReply() {
        if (!this.hasSecondReplyRuleCondition()) {
            return true;
        }
		Rule applicableRule =
                this.context.getApplicableRule();
		RuleContext ruleContext =
                this.context.getRuleContext();
		FirewallAction firewallAction = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
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
		this.context.sendReply(
				Reply.newFailureInstance(
                        ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
		return false;
	}
	
	private boolean canAllowSecondReplyWithinLimit() {
		Rule applicableRule =
                this.context.getApplicableRule();
		RuleContext ruleContext =
                this.context.getRuleContext();
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
				this.context.sendReply(
						Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return false;				
			}
			this.context.addBelowAllowLimitRule(
                    applicableRule);
		}
		return true;
	}
	
	private boolean configureInboundSocket(final Socket inboundSocket) {
		SocketSettings socketSettings = this.getInboundSocketSettings();
		try {
			socketSettings.applyTo(inboundSocket);
		} catch (UnsupportedOperationException | SocketException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the inbound socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private boolean configureListenSocket(final ServerSocket listenSocket) {
		SocketSettings socketSettings = this.getListenSocketSettings();
		try {
			socketSettings.applyTo(listenSocket);
		} catch (UnsupportedOperationException | SocketException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the listen socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}

	private SocketSettings getInboundSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(
				this.context.getApplicableRule(),
                this.context.getSettings());
	}

	private Host getListenBindHost() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(
				this.context.getApplicableRule(),
                this.context.getSettings());
	}
	
	private PortRanges getListenBindPortRanges() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(
				this.context.getApplicableRule(),
                this.context.getSettings());
	}
	
	private SocketSettings getListenSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(
				this.context.getApplicableRule(),
                this.context.getSettings());
	}
	
	private int getRelayBufferSize() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(
				this.context.getApplicableRule(),
                this.context.getSettings()).intValue();
	}
	
	private int getRelayIdleTimeout() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(
				this.context.getApplicableRule(),
                this.context.getSettings()).intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(
						this.context.getApplicableRule(),
                        this.context.getSettings());
		if (relayInboundBandwidthLimit != null) {
			return relayInboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(
						this.context.getApplicableRule(),
                        this.context.getSettings());
		if (relayOutboundBandwidthLimit != null) {
			return relayOutboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private boolean hasSecondReplyRuleCondition() {
		Rule applicableRule =
                this.context.getApplicableRule();
        if (applicableRule.hasRuleCondition(
                SocksRuleConditionSpecConstants.SOCKS_SECOND_REPLY_SERVER_BOUND_ADDRESS)) {
            return true;
        }
        if (applicableRule.hasRuleCondition(
                SocksRuleConditionSpecConstants.SOCKS_SECOND_REPLY_SERVER_BOUND_PORT)) {
            return true;
        }
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_ADDRESS)) {
			return true;
		}
        return applicableRule.hasRuleCondition(
                Socks5RuleConditionSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_PORT);
    }

    @Override
    public void handleRequest() throws IOException {
        ServerSocket listenSocket = null;
        Reply rep;
        Socket inboundSocket = null;
        Socket clientSocket =
                this.context.getClientSocket();
        Reply secondRep;
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
            RuleContext ruleContext =
                    this.context.newReplyRuleContext(
                            rep);
            this.context.setRuleContext(ruleContext);
            Rule applicableRule =
                    this.context.getRules().firstAppliesTo(
                            this.context.getRuleContext());
            if (applicableRule == null) {
                this.serverEventLogger.warn(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "No applicable rule found based on the following "
                                + "context: %s",
                        this.context.getRuleContext()));
                this.context.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
                return;
            }
            this.context.setApplicableRule(applicableRule);
            if (!this.context.canAllowReply()) {
                return;
            }
            if (!this.context.sendReply(rep)) {
                return;
            }
            inboundSocket = this.acceptInboundSocketFrom(listenSocket);
            try {
                listenSocket.close();
            } catch (IOException e) {
                this.serverEventLogger.warn(
                        ObjectLogMessageHelper.objectLogMessage(
                                this, "Error in closing the listen socket"),
                        e);
                this.context.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
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
            this.context.setRuleContext(ruleContext);
            applicableRule =
                    this.context.getRules().firstAppliesTo(
                            this.context.getRuleContext());
            if (applicableRule == null) {
                this.serverEventLogger.warn(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "No applicable rule found based on the following "
                                + "context: %s",
                        this.context.getRuleContext()));
                this.context.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
                return;
            }
            this.context.setApplicableRule(
                    applicableRule);
            if (!this.canAllowSecondReply()) {
                return;
            }
            if (!this.context.sendReply(secondRep)) {
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
                this.context.passData(relay);
            } catch (IOException e) {
                this.serverEventLogger.warn(
                        ObjectLogMessageHelper.objectLogMessage(
                                this, "Error in starting to pass data"),
                        e);
            }
        } finally {
            if (inboundSocket != null && !inboundSocket.isClosed()) {
                inboundSocket.close();
            }
            if (listenSocket != null && !listenSocket.isClosed()) {
                listenSocket.close();
            }
        }
    }

	private Socket limitClientSocket(final Socket clientSocket) {
		Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
		if (outboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					clientSocket, outboundBandwidthLimit);
		}
		return clientSocket;
	}
	
	private Socket limitInboundSocket(final Socket inboundSocket) {
		Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
		if (inboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					inboundSocket, inboundBandwidthLimit);
		}
		return inboundSocket;
	}
	
	private ServerSocket newListenSocket() {
		InetAddress desiredDestinationInetAddress =
				this.resolveDesiredDestinationAddress(
						this.context.getDesiredDestinationAddress().toString());
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
				this.serverEventLogger.warn(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Unable to bind the listen socket to the "
								+ "following host: %s",
								listenBindHost));
				this.context.sendReply(
						Reply.newFailureInstance(
                                ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
				return null;
			}
		}
		int desiredDestinationPort =
                this.context.getDesiredDestinationPort().intValue();
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
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the listen socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return listenSocket;
	}
	
	private ServerSocket newListenSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		ServerSocketFactory listenSocketFactory =
				this.context.getSelectedRoute().getServerSocketFactory();
		ServerSocket listenSocket;
		try {
			listenSocket = listenSocketFactory.newServerSocket();
		} catch (IOException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the listen socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
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
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the listen socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
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
				this.context.getRuleContext());
        String secondReplyServerBoundAddress = secondRep.getServerBoundAddress().toString();
        Port secondReplyServerBoundPort = secondRep.getServerBoundPort();
        secondReplyRuleContext.putRuleArgValue(
                SocksRuleArgSpecConstants.SOCKS_SECOND_REPLY_SERVER_BOUND_ADDRESS,
                secondReplyServerBoundAddress);
        secondReplyRuleContext.putRuleArgValue(
                SocksRuleArgSpecConstants.SOCKS_SECOND_REPLY_SERVER_BOUND_PORT,
                secondReplyServerBoundPort);
		secondReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_ADDRESS,
				secondReplyServerBoundAddress);
		secondReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SECOND_REPLY_SERVER_BOUND_PORT,
				secondReplyServerBoundPort);
		return secondReplyRuleContext;
	}
	
	private InetAddress resolveDesiredDestinationAddress(
			final String desiredDestinationAddress) {
		HostResolverFactory hostResolverFactory =
				this.context.getSelectedRoute().getHostResolverFactory();
		HostResolver hostResolver =	hostResolverFactory.newHostResolver();
		InetAddress desiredDestinationInetAddress;
		try {
			desiredDestinationInetAddress = hostResolver.resolve(
					desiredDestinationAddress);
		} catch (UnknownHostException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to resolve the desired destination "
							+ "address for the listen socket: %s",
							desiredDestinationAddress), 
					e);
			this.context.sendReply(
                    Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(
					e, UnknownHostException.class)) {
				this.serverEventLogger.warn(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Unable to resolve the desired destination "
								+ "address for the listen socket: %s",
								desiredDestinationAddress), 
						e);
				this.context.sendReply(
                        Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
				return null;				
			}
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in resolving the desired destination "
							+ "address for the listen socket: %s",
							desiredDestinationAddress), 
					e);
			this.context.sendReply(
                    Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		}
		return desiredDestinationInetAddress;
	}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [context=" +
                this.context +
                "]";
    }

}
