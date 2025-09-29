package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

final class UdpAssociateRequestHandler extends RequestHandler {

	private static final int HALF_SECOND = 500;

    private final RequestHandlerContext requestHandlerContext;
	private final ServerEventLogger serverEventLogger;
	
	public UdpAssociateRequestHandler(
            final RequestHandlerContext handlerContext) {
        this.requestHandlerContext = handlerContext;
        this.requestHandlerContext.setLogMessageAuthor(this);
		this.serverEventLogger = handlerContext.getServerEventLogger();
	}
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.getClientFacingSocketSettings();
		try {
			socketSettings.applyTo(clientFacingDatagramSock);
		} catch (UnsupportedOperationException | SocketException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the client-facing UDP socket"), 
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private boolean configurePeerFacingDatagramSocket(
			final DatagramSocket peerFacingDatagramSock) {
		SocketSettings socketSettings = this.getPeerFacingSocketSettings();
		try {
			socketSettings.applyTo(peerFacingDatagramSock);
		} catch (UnsupportedOperationException | SocketException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the peer-facing UDP socket"), 
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private Host getClientFacingBindHost() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindHostFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings());
	}
	
	private PortRanges getClientFacingBindPortRanges() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingBindPortRangesFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings());
	}
	
	private SocketSettings getClientFacingSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingSocketSettingsFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings());
	}
	
	private Host getPeerFacingBindHost() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindHostFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings());
	}
	
	private PortRanges getPeerFacingBindPortRanges() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingBindPortRangesFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings());
	}
	
	private SocketSettings getPeerFacingSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestPeerFacingSocketSettingsFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings());
	}
	
	private int getRelayBufferSize() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings()).intValue();
	}
	
	private int getRelayIdleTimeout() {
		return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(
				this.requestHandlerContext.getApplicableRule(),
                this.requestHandlerContext.getSettings()).intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
						this.requestHandlerContext.getApplicableRule(),
                        this.requestHandlerContext.getSettings());
		if (relayInboundBandwidthLimit != null) {
			return relayInboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
						this.requestHandlerContext.getApplicableRule(),
                        this.requestHandlerContext.getSettings());
		if (relayOutboundBandwidthLimit != null) {
			return relayOutboundBandwidthLimit.intValue();
		}
		return null;
	}

    @Override
    public void handleRequest() {
        DatagramSocket peerFacingDatagramSock = null;
        DatagramSocket clientFacingDatagramSock = null;
        Reply rep;
        try {
            peerFacingDatagramSock = this.newPeerFacingDatagramSocket();
            if (peerFacingDatagramSock == null) {
                return;
            }
            clientFacingDatagramSock = this.newClientFacingDatagramSocket();
            if (clientFacingDatagramSock == null) {
                return;
            }
            /*
             * Create a temporary variable to avoid the resource-never-closed warning
             * (The resource will get closed)
             */
            DatagramSocket clientFacingDatagramSck =
                    this.wrapClientFacingDatagramSocket(
                            clientFacingDatagramSock);
            if (clientFacingDatagramSck == null) {
                return;
            }
            clientFacingDatagramSock = clientFacingDatagramSck;
            InetAddress inetAddress =
                    clientFacingDatagramSock.getLocalAddress();
            String serverBoundAddress = inetAddress.getHostAddress();
            int serverBoundPort = clientFacingDatagramSock.getLocalPort();
            rep = Reply.newSuccessInstance(
                    Address.newInstanceFrom(serverBoundAddress),
                    Port.valueOf(serverBoundPort));
            RuleContext ruleContext =
                    this.requestHandlerContext.newReplyRuleContext(rep);
            this.requestHandlerContext.setRuleContext(ruleContext);
            Rule applicableRule =
                    this.requestHandlerContext.getRules().firstAppliesTo(
                            this.requestHandlerContext.getRuleContext());
            if (applicableRule == null) {
                this.serverEventLogger.warn(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "No applicable rule found based on the following "
                                + "context: %s",
                        this.requestHandlerContext.getRuleContext()));
                this.requestHandlerContext.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
                return;
            }
            this.requestHandlerContext.setApplicableRule(applicableRule);
            if (!this.requestHandlerContext.canAllowReply()) {
                return;
            }
            if (!this.requestHandlerContext.sendReply(rep)) {
                return;
            }
            /*
             * Create a temporary variable to avoid the resource-never-closed warning
             * (The resource will get closed)
             */
            DatagramSocket peerFacingDatagramSck =
                    this.limitPeerFacingDatagramSocket(
                            peerFacingDatagramSock);
            if (peerFacingDatagramSck == null) {
                return;
            }
            peerFacingDatagramSock = peerFacingDatagramSck;
            clientFacingDatagramSock = this.limitClientFacingDatagramSocket(
                    clientFacingDatagramSock);
            if (clientFacingDatagramSock == null) {
                return;
            }
            UdpRelayServer.Builder builder = new UdpRelayServer.Builder(
                    this.requestHandlerContext.getDesiredDestinationAddress().toString(),
                    this.requestHandlerContext.getDesiredDestinationPort().intValue(),
                    clientFacingDatagramSock,
                    peerFacingDatagramSock);
            builder.bufferSize(this.getRelayBufferSize());
            NetObjectFactory netObjectFactory =
                    this.requestHandlerContext.getSelectedRoute().getNetObjectFactory();
            builder.hostResolver(netObjectFactory.newHostResolver());
            builder.idleTimeout(this.getRelayIdleTimeout());
            builder.ruleContext(this.requestHandlerContext.getRuleContext());
            builder.rules(this.requestHandlerContext.getRules());
            UdpRelayServer udpRelayServer = builder.build();
            try {
                this.passPackets(udpRelayServer);
            } catch (IOException e) {
                this.serverEventLogger.warn(
                        ObjectLogMessageHelper.objectLogMessage(
                                this, "Error in starting the UDP association"),
                        e);
            }
        } finally {
            if (clientFacingDatagramSock != null
                    && !clientFacingDatagramSock.isClosed()) {
                clientFacingDatagramSock.close();
            }
            if (peerFacingDatagramSock != null
                    && !peerFacingDatagramSock.isClosed()) {
                peerFacingDatagramSock.close();
            }
        }
    }

	private DatagramSocket limitClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
		if (outboundBandwidthLimit != null) {
			try {
				return new BandwidthLimitedDatagramSocket(
						clientFacingDatagramSock,
                        outboundBandwidthLimit);
			} catch (SocketException e) {
				this.serverEventLogger.warn(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in creating the bandwidth-limited "
								+ "client-facing UDP socket"), 
						e);
				return null;
			}
		}		
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket limitPeerFacingDatagramSocket(
			final DatagramSocket peerFacingDatagramSock) {
		Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
		if (inboundBandwidthLimit != null) {
			try {
				return new BandwidthLimitedDatagramSocket(
						peerFacingDatagramSock,
                        inboundBandwidthLimit);
			} catch (SocketException e) {
				this.serverEventLogger.warn(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in creating the bandwidth-limited "
								+ "peer-facing UDP socket"), 
						e);
				return null;
			}
		}		
		return peerFacingDatagramSock;
	}
	
	private DatagramSocket newClientFacingDatagramSocket() {
		Host clientFacingBindHost = this.getClientFacingBindHost();
		InetAddress bindInetAddress;
		try {
			bindInetAddress = clientFacingBindHost.toInetAddress();
		} catch (UnknownHostException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the client-facing UDP socket to "
							+ "the following host: %s",
							clientFacingBindHost));
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		PortRanges bindPortRanges = this.getClientFacingBindPortRanges();
		DatagramSocket clientFacingDatagramSock = null;
		boolean clientFacingDatagramSockBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!clientFacingDatagramSockBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!clientFacingDatagramSockBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					clientFacingDatagramSock =
							this.newClientFacingDatagramSocket(
									bindInetAddress, bindPort);
				} catch (BindException e) {
					continue;
				}
				if (clientFacingDatagramSock == null) {
					return null;
				}
				clientFacingDatagramSockBound = true;
			}
		}
		if (!clientFacingDatagramSockBound) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the client-facing UDP socket to "
							+ "the following address and port (range(s)): "
							+ "%s %s",
							bindInetAddress,
							bindPortRanges));
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;			
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newClientFacingDatagramSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		DatagramSocket clientFacingDatagramSock;
		try {
			clientFacingDatagramSock = new DatagramSocket(null);
		} catch (SocketException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the client-facing UDP "
							+ "socket"), 
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		if (!this.configureClientFacingDatagramSocket(
				clientFacingDatagramSock)) {
			clientFacingDatagramSock.close();
			return null;
		}
		try {
			clientFacingDatagramSock.bind(new InetSocketAddress(
					bindInetAddress, bindPort.intValue()));
		} catch (BindException e) {
			clientFacingDatagramSock.close();
			throw e;
		} catch (SocketException e) {
			if (ThrowableHelper.isOrHasInstanceOf(e, BindException.class)) {
				clientFacingDatagramSock.close();
				throw new BindException();
			}
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the client-facing UDP "
							+ "socket"), 
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			clientFacingDatagramSock.close();
			return null;
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newPeerFacingDatagramSocket() {
		Host peerFacingBindHost = this.getPeerFacingBindHost();
		InetAddress bindInetAddress;
		try {
			bindInetAddress = peerFacingBindHost.toInetAddress();
		} catch (UnknownHostException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the peer-facing UDP socket to "
							+ "the following host: %s",
							peerFacingBindHost));
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		PortRanges bindPortRanges = this.getPeerFacingBindPortRanges();
		DatagramSocket peerFacingDatagramSock = null;
		boolean peerFacingDatagramSockBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!peerFacingDatagramSockBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!peerFacingDatagramSockBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					peerFacingDatagramSock = 
							this.newPeerFacingDatagramSocket(
									bindInetAddress, bindPort);
				} catch (BindException e) {
					continue;
				}
				if (peerFacingDatagramSock == null) {
					return null;
				}
				peerFacingDatagramSockBound = true;
			}
		}
		if (!peerFacingDatagramSockBound) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the peer-facing UDP socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;			
		}
		return peerFacingDatagramSock;
	}
	
	private DatagramSocket newPeerFacingDatagramSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		NetObjectFactory netObjectFactory = 
				this.requestHandlerContext.getSelectedRoute().getNetObjectFactory();
		DatagramSocket peerFacingDatagramSock;
		try {
			peerFacingDatagramSock =
					netObjectFactory.newDatagramSocket(null);
		} catch (SocketException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the peer-facing UDP "
							+ "socket"), 
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		if (!this.configurePeerFacingDatagramSocket(
				peerFacingDatagramSock)) {
			peerFacingDatagramSock.close();
			return null;
		}
		try {
			peerFacingDatagramSock.bind(new InetSocketAddress(
					bindInetAddress, bindPort.intValue()));
		} catch (BindException e) {
			peerFacingDatagramSock.close();
			throw e;
		} catch (SocketException e) {
			if (ThrowableHelper.isOrHasInstanceOf(e, BindException.class)) {
				peerFacingDatagramSock.close();
				throw new BindException();
			}
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the peer-facing UDP "
							+ "socket"), 
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			peerFacingDatagramSock.close();
			return null;
		}
		return peerFacingDatagramSock;
	}

	private void passPackets(
			final UdpRelayServer udpRelayServer) throws IOException {
		try {
			udpRelayServer.start();
			try {
				while (this.requestHandlerContext.getClientSocket().getInputStream().read() != -1) {
					try {
						Thread.sleep(HALF_SECOND);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			} catch (IOException ignored) {
			}
		} finally {
			if (!udpRelayServer.getState().equals(
					UdpRelayServer.State.STOPPED)) {
				udpRelayServer.stop();
			}
		}
	}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [requestHandlerContext=" +
                this.requestHandlerContext +
                "]";
    }

	private DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
		DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory =
				this.requestHandlerContext.getClientFacingDtlsDatagramSocketFactory();
		try {
			clientFacingDatagramSck =
					clientFacingDtlsDatagramSocketFactory.getDatagramSocket(
							clientFacingDatagramSck);
		} catch (IOException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"Error in wrapping the client-facing UDP socket"),
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		try {
			clientFacingDatagramSck = 
					this.requestHandlerContext.getMethodSubNegotiationResults().getDatagramSocket(
							clientFacingDatagramSck);
		} catch (IOException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in wrapping the client-facing UDP socket"), 
					e);
			this.requestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return clientFacingDatagramSck;
	}

}
