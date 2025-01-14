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
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleActionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Relay;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Socks5ValueDerivationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.util.Iterator;

final class ConnectRequestWorker extends TcpBasedRequestWorker {

	private final Logger logger;
	
	public ConnectRequestWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubNegotiationResults methSubNegotiationResults, 
			final Request req) {
		super(socks5Worker, methSubNegotiationResults, req);
		this.logger = LoggerFactory.getLogger(ConnectRequestWorker.class);
	}
	
	private boolean canPrepareTargetFacingSocket() {
		Boolean b = this.getApplicableRule().getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET);
		if (b != null) {
			return b.booleanValue();
		}
		b = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_PREPARE_TARGET_FACING_SOCKET);
		return b.booleanValue();
	}

	private boolean configureTargetFacingSocket(
			final Socket targetFacingSocket) {
		SocketSettings socketSettings = this.getTargetFacingSocketSettings();
		try {
			socketSettings.applyTo(targetFacingSocket);
		} catch (UnsupportedOperationException | SocketException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the target-facing socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private int getRelayBufferSize() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(
				this.getApplicableRule(), this.getSettings()).intValue();
	}
	
	private int getRelayIdleTimeout() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(
				this.getApplicableRule(), this.getSettings()).intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(
						this.getApplicableRule(), this.getSettings());
		if (relayInboundBandwidthLimit != null) {
			return relayInboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(
						this.getApplicableRule(), this.getSettings());
		if (relayOutboundBandwidthLimit != null) {
			return relayOutboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Host getTargetFacingBindHost() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(
				this.getApplicableRule(), this.getSettings());
	}
	
	private PortRanges getTargetFacingBindPortRanges() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(
				this.getApplicableRule(), this.getSettings());
	}
	
	private int getTargetFacingConnectTimeout() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger connectTimeout =
				applicableRule.getLastRuleActionValue(
						Socks5RuleActionSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT);
		if (connectTimeout != null) {
			return connectTimeout.intValue();
		}
		connectTimeout = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_REQUEST_TARGET_FACING_CONNECT_TIMEOUT);
		return connectTimeout.intValue();
	}
	
	private SocketSettings getTargetFacingSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(
				this.getApplicableRule(), this.getSettings());
	}

	private Socket limitClientSocket(final Socket clientSocket) {
		Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
		if (outboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					clientSocket, outboundBandwidthLimit.intValue());
		}
		return clientSocket;
	}
	
	private Socket limitTargetFacingSocket(final Socket targetFacingSocket) {
		Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
		if (inboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					targetFacingSocket, inboundBandwidthLimit.intValue());
		}
		return targetFacingSocket;
	}
	
	private Socket newExtemporaneousTargetFacingSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		NetObjectFactory netObjectFactory =
				this.getSelectedRoute().getNetObjectFactory();
		Socket targetFacingSocket = null;
		try {
			targetFacingSocket = netObjectFactory.newSocket(
					this.getDesiredDestinationAddress().toString(), 
					this.getDesiredDestinationPort().intValue(), 
					bindInetAddress, 
					bindPort.intValue());
		} catch (UnknownHostException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the target-facing "
							+ "socket"), 
					e);
			this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(
					e, BindException.class)) {
				throw new BindException();
			}
			if (ThrowableHelper.isOrHasInstanceOf(
					e, UnknownHostException.class)) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in creating the target-facing "
								+ "socket"), 
						e);
				this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
				return null;				
			}
			if (ThrowableHelper.isOrHasInstanceOf(
					e, SocketException.class)) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the target-facing "
								+ "socket"), 
						e);
				this.sendReply(Reply.newFailureInstance(ReplyCode.NETWORK_UNREACHABLE));
				return null;						
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in connecting the target-facing "
							+ "socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return targetFacingSocket;
	}
	
	private Socket newExtemporaneousTargetFacingSocket(
			final InetAddress bindInetAddress, 
			final PortRanges bindPortRanges) {
		Socket targetFacingSocket = null;
		boolean targetFacingSocketBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!targetFacingSocketBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!targetFacingSocketBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					targetFacingSocket = 
							this.newExtemporaneousTargetFacingSocket(
									bindInetAddress, bindPort);
				} catch (BindException e) {
					continue;
				}
				if (targetFacingSocket == null) {
					return null;
				}
				targetFacingSocketBound = true;
			}
		}
		if (!targetFacingSocketBound) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the target-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;			
		}
		return targetFacingSocket;
	}
	
	private Socket newPreparedTargetFacingSocket(
			final InetAddress bindInetAddress, 
			final Port bindPort) throws SocketException {
		InetAddress desiredDestinationInetAddress =
				this.resolveDesiredDestinationAddress(
						this.getDesiredDestinationAddress().toString());
		if (desiredDestinationInetAddress == null) {
			return null;
		}
		NetObjectFactory netObjectFactory = 
				this.getSelectedRoute().getNetObjectFactory();
		Socket targetFacingSocket = null;
		int connectTimeout = this.getTargetFacingConnectTimeout();		
		targetFacingSocket = netObjectFactory.newSocket();
		if (!this.configureTargetFacingSocket(targetFacingSocket)) {
			try {
				targetFacingSocket.close();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			return null;
		}
		try {
			targetFacingSocket.bind(new InetSocketAddress(
					bindInetAddress, bindPort.intValue()));
		} catch (SocketException e) {
			try {
				targetFacingSocket.close();
			} catch (IOException ex) {
				throw new AssertionError(ex);
			}
			throw e;
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(e, SocketException.class)) {
				try {
					targetFacingSocket.close();
				} catch (IOException ex) {
					throw new AssertionError(ex);
				}
				throw new SocketException();
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the target-facing "
							+ "socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			try {
				targetFacingSocket.close();
			} catch (IOException ex) {
				throw new AssertionError(ex);
			}					
			return null;
		}				
		try {
			targetFacingSocket.connect(new InetSocketAddress(
					desiredDestinationInetAddress,
					this.getDesiredDestinationPort().intValue()),
					connectTimeout);
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(
					e, BindException.class)) {
				try {
					targetFacingSocket.close();
				} catch (IOException ex) {
					throw new AssertionError(ex);
				}
				throw new BindException();
			}
			if (ThrowableHelper.isOrHasInstanceOf(
					e, SocketException.class)) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the target-facing "
								+ "socket"), 
						e);
				this.sendReply(Reply.newFailureInstance(ReplyCode.NETWORK_UNREACHABLE));
				try {
					targetFacingSocket.close();
				} catch (IOException ex) {
					throw new AssertionError(ex);
				}						
				return null;						
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in connecting the target-facing "
							+ "socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			try {
				targetFacingSocket.close();
			} catch (IOException ex) {
				throw new AssertionError(ex);
			}
			return null;
		}
		return targetFacingSocket;
	}
	
	private Socket newPreparedTargetFacingSocket(
			final InetAddress bindInetAddress, 
			final PortRanges bindPortRanges) {
		Socket targetFacingSocket = null;
		boolean targetFacingSocketBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!targetFacingSocketBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!targetFacingSocketBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					targetFacingSocket = this.newPreparedTargetFacingSocket(
							bindInetAddress, bindPort);
				} catch (SocketException e) {
					continue;
				}
				if (targetFacingSocket == null) {
					return null;
				}
				targetFacingSocketBound = true;
			}
		}
		if (!targetFacingSocketBound) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the target-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return targetFacingSocket;
	}
	
	private Socket newTargetFacingSocket() {
		Host targetFacingBindHost = this.getTargetFacingBindHost();
		InetAddress bindInetAddress = null;
		try {
			bindInetAddress = targetFacingBindHost.toInetAddress();
		} catch (UnknownHostException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the target-facing socket to the "
							+ "following host: %s",
							targetFacingBindHost));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		PortRanges bindPortRanges = this.getTargetFacingBindPortRanges();
		return this.canPrepareTargetFacingSocket() ? 
				this.newPreparedTargetFacingSocket(
						bindInetAddress, bindPortRanges)	
				: this.newExtemporaneousTargetFacingSocket(
						bindInetAddress, bindPortRanges);
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
							+ "address for the target-facing socket: %s",
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
								+ "address for the target-facing socket: %s",
								desiredDestinationAddress), 
						e);
				this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
				return null;				
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in resolving the desired destination "
							+ "address for the target-facing socket: %s",
							desiredDestinationAddress), 
					e);
			this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		}
		return desiredDestinationInetAddress;
	}
	
	@Override
	public void run() {
		Socket targetFacingSocket = null;
		Socket clientSocket = this.getClientSocket();		
		Reply rep = null;
		try {
			targetFacingSocket = this.newTargetFacingSocket();
			if (targetFacingSocket == null) {
				return;
			}
			String serverBoundAddress = 
					targetFacingSocket.getInetAddress().getHostAddress();
			int serverBoundPort = targetFacingSocket.getPort();
			rep = Reply.newSuccessInstance(
					Address.newInstanceFrom(serverBoundAddress),
					Port.valueOf(serverBoundPort));
			RuleContext ruleContext = this.newReplyRuleContext(
					rep);
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
			/*
			 * Create a temporary variable to avoid the resource-never-closed warning
			 * (The resource will get closed)
			 */
			Socket targetFacingSock = this.limitTargetFacingSocket(
					targetFacingSocket);
			targetFacingSocket = targetFacingSock;
			clientSocket = this.limitClientSocket(clientSocket);
			Relay.Builder builder = new Relay.Builder(
					clientSocket, targetFacingSocket);
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
			if (targetFacingSocket != null && !targetFacingSocket.isClosed()) {
				try {
					targetFacingSocket.close();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}
	}

}
