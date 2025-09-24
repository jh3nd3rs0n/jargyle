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
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Relay;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

final class BindRequestHandler extends RequestHandler {
	
	private final ServerEventLogger serverEventLogger;
    private final TcpBasedRequestHandlerContext tcpBasedRequestHandlerContext;
		
	public BindRequestHandler(
			final TcpBasedRequestHandlerContext handlerContext) {
		this.serverEventLogger = handlerContext.getServerEventLogger();
        this.tcpBasedRequestHandlerContext = handlerContext;
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
			this.tcpBasedRequestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return inboundSocket;
	}
	
	private boolean canAllowSecondReply() {
		Rule applicableRule =
                this.tcpBasedRequestHandlerContext.getApplicableRule();
		RuleContext ruleContext =
                this.tcpBasedRequestHandlerContext.getRuleContext();
		if (!this.hasSecondReplyRuleCondition()) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			this.tcpBasedRequestHandlerContext.sendReply(
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
		this.tcpBasedRequestHandlerContext.sendReply(
				Reply.newFailureInstance(
                        ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
		return false;
	}
	
	private boolean canAllowSecondReplyWithinLimit() {
		Rule applicableRule =
                this.tcpBasedRequestHandlerContext.getApplicableRule();
		RuleContext ruleContext =
                this.tcpBasedRequestHandlerContext.getRuleContext();
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
				this.tcpBasedRequestHandlerContext.sendReply(
						Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return false;				
			}
			this.tcpBasedRequestHandlerContext.addBelowAllowLimitRule(
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
			this.tcpBasedRequestHandlerContext.sendReply(
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
			this.tcpBasedRequestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}

	private SocketSettings getInboundSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestInboundSocketSettingsFrom(
				this.tcpBasedRequestHandlerContext.getApplicableRule(),
                this.tcpBasedRequestHandlerContext.getSettings());
	}

	private Host getListenBindHost() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindHostFrom(
				this.tcpBasedRequestHandlerContext.getApplicableRule(),
                this.tcpBasedRequestHandlerContext.getSettings());
	}
	
	private PortRanges getListenBindPortRanges() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenBindPortRangesFrom(
				this.tcpBasedRequestHandlerContext.getApplicableRule(),
                this.tcpBasedRequestHandlerContext.getSettings());
	}
	
	private SocketSettings getListenSocketSettings() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestListenSocketSettingsFrom(
				this.tcpBasedRequestHandlerContext.getApplicableRule(),
                this.tcpBasedRequestHandlerContext.getSettings());
	}
	
	private int getRelayBufferSize() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayBufferSizeFrom(
				this.tcpBasedRequestHandlerContext.getApplicableRule(),
                this.tcpBasedRequestHandlerContext.getSettings()).intValue();
	}
	
	private int getRelayIdleTimeout() {
		return Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayIdleTimeoutFrom(
				this.tcpBasedRequestHandlerContext.getApplicableRule(),
                this.tcpBasedRequestHandlerContext.getSettings()).intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayInboundBandwidthLimitFrom(
						this.tcpBasedRequestHandlerContext.getApplicableRule(),
                        this.tcpBasedRequestHandlerContext.getSettings());
		if (relayInboundBandwidthLimit != null) {
			return relayInboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				Socks5ValueDerivationHelper.getSocks5OnBindRequestRelayOutboundBandwidthLimitFrom(
						this.tcpBasedRequestHandlerContext.getApplicableRule(),
                        this.tcpBasedRequestHandlerContext.getSettings());
		if (relayOutboundBandwidthLimit != null) {
			return relayOutboundBandwidthLimit.intValue();
		}
		return null;
	}
	
	private boolean hasSecondReplyRuleCondition() {
		Rule applicableRule =
                this.tcpBasedRequestHandlerContext.getApplicableRule();
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
                this.tcpBasedRequestHandlerContext.getClientSocket();
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
                    this.tcpBasedRequestHandlerContext.newReplyRuleContext(
                            rep);
            this.tcpBasedRequestHandlerContext.setRuleContext(ruleContext);
            Rule applicableRule =
                    this.tcpBasedRequestHandlerContext.getRules().firstAppliesTo(
                            this.tcpBasedRequestHandlerContext.getRuleContext());
            if (applicableRule == null) {
                this.serverEventLogger.warn(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "No applicable rule found based on the following "
                                + "context: %s",
                        this.tcpBasedRequestHandlerContext.getRuleContext()));
                this.tcpBasedRequestHandlerContext.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
                return;
            }
            this.tcpBasedRequestHandlerContext.setApplicableRule(applicableRule);
            if (!this.tcpBasedRequestHandlerContext.canAllowReply()) {
                return;
            }
            if (!this.tcpBasedRequestHandlerContext.sendReply(rep)) {
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
                this.tcpBasedRequestHandlerContext.sendReply(
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
            this.tcpBasedRequestHandlerContext.setRuleContext(ruleContext);
            applicableRule =
                    this.tcpBasedRequestHandlerContext.getRules().firstAppliesTo(
                            this.tcpBasedRequestHandlerContext.getRuleContext());
            if (applicableRule == null) {
                this.serverEventLogger.warn(ObjectLogMessageHelper.objectLogMessage(
                        this,
                        "No applicable rule found based on the following "
                                + "context: %s",
                        this.tcpBasedRequestHandlerContext.getRuleContext()));
                this.tcpBasedRequestHandlerContext.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
                return;
            }
            this.tcpBasedRequestHandlerContext.setApplicableRule(
                    applicableRule);
            if (!this.canAllowSecondReply()) {
                return;
            }
            if (!this.tcpBasedRequestHandlerContext.sendReply(secondRep)) {
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
                this.tcpBasedRequestHandlerContext.passData(relay);
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
						this.tcpBasedRequestHandlerContext.getDesiredDestinationAddress().toString());
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
				this.tcpBasedRequestHandlerContext.sendReply(
						Reply.newFailureInstance(
                                ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
				return null;
			}
		}
		int desiredDestinationPort =
                this.tcpBasedRequestHandlerContext.getDesiredDestinationPort().intValue();
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
			this.tcpBasedRequestHandlerContext.sendReply(
					Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return listenSocket;
	}
	
	private ServerSocket newListenSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		NetObjectFactory netObjectFactory =
				this.tcpBasedRequestHandlerContext.getSelectedRoute().getNetObjectFactory();
		ServerSocket listenSocket;
		try {
			listenSocket = netObjectFactory.newServerSocket();
		} catch (IOException e) {
			this.serverEventLogger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the listen socket"), 
					e);
			this.tcpBasedRequestHandlerContext.sendReply(
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
			this.tcpBasedRequestHandlerContext.sendReply(
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
				this.tcpBasedRequestHandlerContext.getRuleContext());
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
				this.tcpBasedRequestHandlerContext.getSelectedRoute().getNetObjectFactory();
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
							+ "address for the listen socket: %s",
							desiredDestinationAddress), 
					e);
			this.tcpBasedRequestHandlerContext.sendReply(
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
				this.tcpBasedRequestHandlerContext.sendReply(
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
			this.tcpBasedRequestHandlerContext.sendReply(
                    Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return null;
		}
		return desiredDestinationInetAddress;
	}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [tcpBasedRequestHandlerContext=" +
                this.tcpBasedRequestHandlerContext +
                "]";
    }

}
