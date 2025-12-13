package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Relay;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

final class ConnectRequestHandler extends RequestHandler {

    private final RelayRequestHandlerContext context;
	private final ServerEventLogger serverEventLogger;

	public ConnectRequestHandler(final RelayRequestHandlerContext cntxt) {
        this.context = cntxt;
        this.context.setLogMessageSource(this);
		this.serverEventLogger = cntxt.getServerEventLogger();
	}
	
	private boolean canPrepareTargetFacingSocket() {
        return Socks5ValueDerivationHelper.getSocks5OnConnectRequestPrepareTargetFacingSocketFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
	}

	private boolean configureTargetFacingSocket(
			final Socket targetFacingSocket) {
		SocketSettings socketSettings = this.getTargetFacingSocketSettings();
		try {
			socketSettings.applyTo(targetFacingSocket);
		} catch (UnsupportedOperationException | SocketException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the target-facing socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private int getRelayBufferSize() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayBufferSizeFrom(
				this.context.getApplicableRule(),
                this.context.getSettings()).intValue();
	}
	
	private int getRelayIdleTimeout() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayIdleTimeoutFrom(
				this.context.getApplicableRule(),
                this.context.getSettings()).intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayInboundBandwidthLimitFrom(
						this.context.getApplicableRule(),
                        this.context.getSettings());
		if (relayInboundBandwidthLimit != null) {
			return relayInboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnConnectRequestRelayOutboundBandwidthLimitFrom(
						this.context.getApplicableRule(),
                        this.context.getSettings());
		if (relayOutboundBandwidthLimit != null) {
			return relayOutboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Host getTargetFacingBindHost() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindHostFrom(
				this.context.getApplicableRule(),
                this.context.getSettings());
	}
	
	private PortRanges getTargetFacingBindPortRanges() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingBindPortRangesFrom(
				this.context.getApplicableRule(),
                this.context.getSettings());
	}
	
	private int getTargetFacingConnectTimeout() {
        return Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingConnectTimeoutFrom(
                this.context.getApplicableRule(),
                this.context.getSettings())
                .intValue();
	}
	
	private SocketSettings getTargetFacingSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnConnectRequestTargetFacingSocketSettingsFrom(
				this.context.getApplicableRule(),
                this.context.getSettings());
	}

    @Override
    public void handleRequest() throws IOException {
        Socket targetFacingSocket = null;
        Socket clientSocket = this.context.getClientSocket();
        Reply rep;
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
            this.context.setApplicableRule(
                    applicableRule);
            if (!this.context.canAllowReply()) {
                return;
            }
            if (!this.context.sendReply(rep)) {
                return;
            }
            targetFacingSocket = this.limitTargetFacingSocket(
                    targetFacingSocket);
            clientSocket = this.limitClientSocket(clientSocket);
            Relay.Builder builder = new Relay.Builder(
                    clientSocket, targetFacingSocket);
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
            if (targetFacingSocket != null && !targetFacingSocket.isClosed()) {
                targetFacingSocket.close();
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
	
	private Socket limitTargetFacingSocket(final Socket targetFacingSocket) {
		Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
		if (inboundBandwidthLimit != null) {
			return new BandwidthLimitedSocket(
					targetFacingSocket, inboundBandwidthLimit);
		}
		return targetFacingSocket;
	}
	
	private Socket newExtemporaneousTargetFacingSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		NetObjectFactory netObjectFactory =
				this.context.getSelectedRoute().getNetObjectFactory();
		Socket targetFacingSocket;
		try {
			targetFacingSocket = netObjectFactory.newSocket(
					this.context.getDesiredDestinationAddress().toString(),
					this.context.getDesiredDestinationPort().intValue(),
					bindInetAddress, 
					bindPort.intValue());
		} catch (UnknownHostException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the target-facing "
							+ "socket"), 
					e);
			this.context.sendReply(
                    Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(
					e, BindException.class)) {
				throw new BindException();
			}
			if (ThrowableHelper.isOrHasInstanceOf(
					e, UnknownHostException.class)) {
				this.serverEventLogger.warn(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in creating the target-facing "
								+ "socket"), 
						e);
				this.context.sendReply(
                        Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
				return null;				
			}
			if (ThrowableHelper.isOrHasInstanceOf(
					e, SocketException.class)) {
				this.serverEventLogger.warn(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the target-facing "
								+ "socket"), 
						e);
				this.context.sendReply(
                        Reply.newFailureInstance(ReplyCode.NETWORK_UNREACHABLE));
				return null;						
			}
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in connecting the target-facing "
							+ "socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
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
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the target-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;			
		}
		return targetFacingSocket;
	}
	
	private Socket newPreparedTargetFacingSocket(
			final InetAddress bindInetAddress, 
			final Port bindPort) throws SocketException {
		InetAddress desiredDestinationInetAddress =
				this.resolveDesiredDestinationAddress(
						this.context.getDesiredDestinationAddress().toString());
		if (desiredDestinationInetAddress == null) {
			return null;
		}
		NetObjectFactory netObjectFactory = 
				this.context.getSelectedRoute().getNetObjectFactory();
		Socket targetFacingSocket;
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
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the target-facing "
							+ "socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
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
					this.context.getDesiredDestinationPort().intValue()),
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
				this.serverEventLogger.warn(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the target-facing "
								+ "socket"), 
						e);
				this.context.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.NETWORK_UNREACHABLE));
				try {
					targetFacingSocket.close();
				} catch (IOException ex) {
					throw new AssertionError(ex);
				}						
				return null;						
			}
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in connecting the target-facing "
							+ "socket"), 
					e);
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
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
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the target-facing socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return targetFacingSocket;
	}
	
	private Socket newTargetFacingSocket() {
		Host targetFacingBindHost = this.getTargetFacingBindHost();
		InetAddress bindInetAddress;
		try {
			bindInetAddress = targetFacingBindHost.toInetAddress();
		} catch (UnknownHostException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the target-facing socket to the "
							+ "following host: %s",
							targetFacingBindHost));
			this.context.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
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
				this.context.getSelectedRoute().getNetObjectFactory();
		HostResolver hostResolver =	netObjectFactory.newHostResolver();
		InetAddress desiredDestinationInetAddress;
		try {
			desiredDestinationInetAddress = hostResolver.resolve(
					desiredDestinationAddress);
		} catch (UnknownHostException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to resolve the desired destination "
							+ "address for the target-facing socket: %s",
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
								+ "address for the target-facing socket: %s",
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
							+ "address for the target-facing socket: %s",
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
