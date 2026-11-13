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

    private Socket acceptRemoteSocketFrom(final ServerSocket remoteFacingServerSocket) {
        Socket remoteSocket;
        try {
            remoteSocket = remoteFacingServerSocket.accept();
        } catch (IOException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in waiting for a remote socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        return remoteSocket;
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

    private boolean configureRemoteFacingServerSocket(final ServerSocket remoteFacingServerSocket) {
        SocketSettings socketSettings = this.getRemoteFacingServerSocketSettings();
        try {
            socketSettings.applyTo(remoteFacingServerSocket);
        } catch (UnsupportedOperationException | SocketException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in setting the remote facing server socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return false;
        }
        return true;
    }

    private boolean configureRemoteSocket(final Socket remoteSocket) {
        SocketSettings socketSettings = this.getRemoteSocketSocketSettings();
        try {
            socketSettings.applyTo(remoteSocket);
        } catch (UnsupportedOperationException | SocketException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this, "Error in setting the remote socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return false;
        }
        return true;
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

    private Host getRemoteFacingServerSocketBindHost() {
        return Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindHostFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private PortRanges getRemoteFacingServerSocketBindPortRanges() {
        return Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketBindPortRangesFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private SocketSettings getRemoteFacingServerSocketSettings() {
        return Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteFacingServerSocketSocketSettingsFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private SocketSettings getRemoteSocketSocketSettings() {
        return Socks5ValueDerivationHelper.getSocks5OnBindRequestRemoteSocketSocketSettingsFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    @Override
    public void handleRequest() throws IOException {
        ServerSocket remoteFacingServerSocket = null;
        Reply rep;
        Socket remoteSocket = null;
        Socket clientSocket =
                this.context.getClientSocket();
        Reply secondRep;
        try {
            remoteFacingServerSocket = this.newRemoteFacingServerSocket();
            if (remoteFacingServerSocket == null) {
                return;
            }
            InetAddress inetAddress = remoteFacingServerSocket.getInetAddress();
            String serverBoundAddress = inetAddress.getHostAddress();
            int serverBoundPort = remoteFacingServerSocket.getLocalPort();
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
            remoteSocket = this.acceptRemoteSocketFrom(remoteFacingServerSocket);
            try {
                remoteFacingServerSocket.close();
            } catch (IOException e) {
                this.serverEventLogger.warn(
                        ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Error in closing the remote facing server socket"),
                        e);
                this.context.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
                return;
            }
            if (remoteSocket == null) {
                return;
            }
            if (!this.configureRemoteSocket(remoteSocket)) {
                return;
            }
            serverBoundAddress =
                    remoteSocket.getInetAddress().getHostAddress();
            serverBoundPort = remoteSocket.getPort();
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
            remoteSocket = this.limitRemoteSocket(remoteSocket);
            clientSocket = this.limitClientSocket(clientSocket);
            Relay.Builder builder = new Relay.Builder(
                    clientSocket, remoteSocket);
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
            if (remoteSocket != null && !remoteSocket.isClosed()) {
                remoteSocket.close();
            }
            if (remoteFacingServerSocket != null && !remoteFacingServerSocket.isClosed()) {
                remoteFacingServerSocket.close();
            }
        }
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

    private Socket limitClientSocket(final Socket clientSocket) {
        Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
        if (outboundBandwidthLimit != null) {
            return new BandwidthLimitedSocket(
                    clientSocket, outboundBandwidthLimit);
        }
        return clientSocket;
    }

    private Socket limitRemoteSocket(final Socket remoteSocket) {
        Integer remoteBandwidthLimit = this.getRelayInboundBandwidthLimit();
        if (remoteBandwidthLimit != null) {
            return new BandwidthLimitedSocket(
                    remoteSocket, remoteBandwidthLimit);
        }
        return remoteSocket;
    }

    private ServerSocket newRemoteFacingServerSocket() {
        InetAddress desiredDestinationInetAddress =
                this.resolveDesiredDestinationAddress(
                        this.context.getDesiredDestinationAddress().toString());
        if (desiredDestinationInetAddress == null) {
            return null;
        }
        InetAddress bindInetAddress = desiredDestinationInetAddress;
        if (HostAddress.isAllZerosHostAddress(
                desiredDestinationInetAddress.getHostAddress())) {
            Host remoteFacingServerSocketBindHost = this.getRemoteFacingServerSocketBindHost();
            try {
                bindInetAddress = remoteFacingServerSocketBindHost.toInetAddress();
            } catch (UnknownHostException e) {
                this.serverEventLogger.warn(
                        ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Unable to bind the remote facing server socket "
                                        + "to the following host: %s",
                                remoteFacingServerSocketBindHost));
                this.context.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
                return null;
            }
        }
        int desiredDestinationPort =
                this.context.getDesiredDestinationPort().intValue();
        PortRanges bindPortRanges = (desiredDestinationPort == 0) ?
                this.getRemoteFacingServerSocketBindPortRanges() : PortRanges.of(
                PortRange.of(Port.valueOf(
                        desiredDestinationPort)));
        ServerSocket remoteFacingServerSocket = null;
        boolean remoteFacingServerSocketBound = false;
        for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
             !remoteFacingServerSocketBound && iterator.hasNext(); ) {
            PortRange bindPortRange = iterator.next();
            for (Iterator<Port> iter = bindPortRange.iterator();
                 !remoteFacingServerSocketBound && iter.hasNext(); ) {
                Port bindPort = iter.next();
                try {
                    remoteFacingServerSocket = this.newRemoteFacingServerSocket(
                            bindInetAddress, bindPort);
                } catch (BindException e) {
                    continue;
                }
                if (remoteFacingServerSocket == null) {
                    return null;
                }
                remoteFacingServerSocketBound = true;
            }
        }
        if (!remoteFacingServerSocketBound) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to bind the remote facing server socket "
                                    + "to the following address and port "
                                    + "(range(s)): %s %s",
                            bindInetAddress,
                            bindPortRanges));
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        return remoteFacingServerSocket;
    }

    private ServerSocket newRemoteFacingServerSocket(
            final InetAddress bindInetAddress,
            final Port bindPort) throws BindException {
        ServerSocketFactory remoteFacingServerSocketFactory =
                this.context.getSelectedRoute().getServerSocketFactory();
        ServerSocket remoteFacingServerSocket;
        try {
            remoteFacingServerSocket = remoteFacingServerSocketFactory.newServerSocket();
        } catch (IOException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in creating the remote facing server socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        if (!this.configureRemoteFacingServerSocket(remoteFacingServerSocket)) {
            try {
                remoteFacingServerSocket.close();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            return null;
        }
        try {
            remoteFacingServerSocket.bind(new InetSocketAddress(
                    bindInetAddress, bindPort.intValue()));
        } catch (BindException e) {
            try {
                remoteFacingServerSocket.close();
            } catch (IOException ex) {
                throw new AssertionError(ex);
            }
            throw e;
        } catch (IOException e) {
            if (ThrowableHelper.isOrHasInstanceOf(e, BindException.class)) {
                try {
                    remoteFacingServerSocket.close();
                } catch (IOException ex) {
                    throw new AssertionError(ex);
                }
                throw new BindException();
            }
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in binding the remote facing server socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            try {
                remoteFacingServerSocket.close();
            } catch (IOException ex) {
                throw new AssertionError(ex);
            }
            return null;
        }
        return remoteFacingServerSocket;
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
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress desiredDestinationInetAddress;
        try {
            desiredDestinationInetAddress = hostResolver.resolve(
                    desiredDestinationAddress);
        } catch (UnknownHostException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to resolve the desired destination "
                                    + "address for the remote facing server socket: %s",
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
                                        + "address for the remote facing server socket: %s",
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
                                    + "address for the remote facing server socket: %s",
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
