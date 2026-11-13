package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

final class UdpAssociateRequestHandler extends RequestHandler {

    private static final int HALF_SECOND = 500;

    private final RequestHandlerContext context;
    private final ServerEventLogger serverEventLogger;

    public UdpAssociateRequestHandler(final RequestHandlerContext cntxt) {
        this.context = cntxt;
        this.context.setLogMessageSource(this);
        this.serverEventLogger = cntxt.getServerEventLogger();
    }

    private boolean configureClientFacingDatagramSocket(
            final DatagramSocket clientFacingDatagramSock) {
        SocketSettings socketSettings = this.getClientFacingDatagramSocketSocketSettings();
        try {
            socketSettings.applyTo(clientFacingDatagramSock);
        } catch (UnsupportedOperationException | SocketException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in setting the client facing UDP socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return false;
        }
        return true;
    }

    private boolean configureRemoteFacingDatagramSocket(
            final DatagramSocket remoteFacingDatagramSock) {
        SocketSettings socketSettings = this.getRemoteFacingDatagramSocketSocketSettings();
        try {
            socketSettings.applyTo(remoteFacingDatagramSock);
        } catch (UnsupportedOperationException | SocketException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in setting the remote facing UDP socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return false;
        }
        return true;
    }

    private Host getClientFacingDatagramSocketBindHost() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindHostFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private PortRanges getClientFacingDatagramSocketBindPortRanges() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketBindPortRangesFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private SocketSettings getClientFacingDatagramSocketSocketSettings() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestClientFacingDatagramSocketSocketSettingsFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private int getRelayBufferSize() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayBufferSizeFrom(
                this.context.getApplicableRule(),
                this.context.getSettings()).intValue();
    }

    private int getRelayIdleTimeout() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayIdleTimeoutFrom(
                this.context.getApplicableRule(),
                this.context.getSettings()).intValue();
    }

    private Integer getRelayInboundBandwidthLimit() {
        PositiveInteger relayInboundBandwidthLimit =
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayInboundBandwidthLimitFrom(
                        this.context.getApplicableRule(),
                        this.context.getSettings());
        if (relayInboundBandwidthLimit != null) {
            return relayInboundBandwidthLimit.intValue();
        }
        return null;
    }

    private Integer getRelayOutboundBandwidthLimit() {
        PositiveInteger relayOutboundBandwidthLimit =
                Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRelayOutboundBandwidthLimitFrom(
                        this.context.getApplicableRule(),
                        this.context.getSettings());
        if (relayOutboundBandwidthLimit != null) {
            return relayOutboundBandwidthLimit.intValue();
        }
        return null;
    }

    private Host getRemoteFacingDatagramSocketBindHost() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindHostFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private PortRanges getRemoteFacingDatagramSocketBindPortRanges() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketBindPortRangesFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private SocketSettings getRemoteFacingDatagramSocketSocketSettings() {
        return Socks5ValueDerivationHelper.getSocks5OnUdpAssociateRequestRemoteFacingDatagramSocketSocketSettingsFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    @Override
    public void handleRequest() {
        DatagramSocket remoteFacingDatagramSock = null;
        DatagramSocket clientFacingDatagramSock = null;
        Reply rep;
        try {
            remoteFacingDatagramSock = this.newRemoteFacingDatagramSocket();
            if (remoteFacingDatagramSock == null) {
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
                    this.context.newReplyRuleContext(rep);
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
            /*
             * Create a temporary variable to avoid the resource-never-closed warning
             * (The resource will get closed)
             */
            DatagramSocket remoteFacingDatagramSck =
                    this.limitRemoteFacingDatagramSocket(
                            remoteFacingDatagramSock);
            if (remoteFacingDatagramSck == null) {
                return;
            }
            remoteFacingDatagramSock = remoteFacingDatagramSck;
            clientFacingDatagramSock = this.limitClientFacingDatagramSocket(
                    clientFacingDatagramSock);
            if (clientFacingDatagramSock == null) {
                return;
            }
            UdpRelayServer.Builder builder = new UdpRelayServer.Builder(
                    this.context.getDesiredDestinationAddress().toString(),
                    this.context.getDesiredDestinationPort().intValue(),
                    clientFacingDatagramSock,
                    remoteFacingDatagramSock);
            builder.bufferSize(this.getRelayBufferSize());
            HostResolverFactory hostResolverFactory =
                    this.context.getSelectedRoute().getHostResolverFactory();
            builder.hostResolver(hostResolverFactory.newHostResolver());
            builder.idleTimeout(this.getRelayIdleTimeout());
            builder.ruleContext(this.context.getRuleContext());
            builder.rules(this.context.getRules());
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
            if (remoteFacingDatagramSock != null
                    && !remoteFacingDatagramSock.isClosed()) {
                remoteFacingDatagramSock.close();
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
                                        + "client facing UDP socket"),
                        e);
                return null;
            }
        }
        return clientFacingDatagramSock;
    }

    private DatagramSocket limitRemoteFacingDatagramSocket(
            final DatagramSocket remoteFacingDatagramSock) {
        Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
        if (inboundBandwidthLimit != null) {
            try {
                return new BandwidthLimitedDatagramSocket(
                        remoteFacingDatagramSock,
                        inboundBandwidthLimit);
            } catch (SocketException e) {
                this.serverEventLogger.warn(
                        ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Error in creating the bandwidth-limited "
                                        + "remote facing UDP socket"),
                        e);
                return null;
            }
        }
        return remoteFacingDatagramSock;
    }

    private DatagramSocket newClientFacingDatagramSocket() {
        Host clientFacingDatagramSocketBindHost = this.getClientFacingDatagramSocketBindHost();
        InetAddress bindInetAddress;
        try {
            bindInetAddress = clientFacingDatagramSocketBindHost.toInetAddress();
        } catch (UnknownHostException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to bind the client facing UDP socket to "
                                    + "the following host: %s",
                            clientFacingDatagramSocketBindHost));
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        PortRanges bindPortRanges = this.getClientFacingDatagramSocketBindPortRanges();
        DatagramSocket clientFacingDatagramSock = null;
        boolean clientFacingDatagramSockBound = false;
        for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
             !clientFacingDatagramSockBound && iterator.hasNext(); ) {
            PortRange bindPortRange = iterator.next();
            for (Iterator<Port> iter = bindPortRange.iterator();
                 !clientFacingDatagramSockBound && iter.hasNext(); ) {
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
                            "Unable to bind the client facing UDP socket to "
                                    + "the following address and port (range(s)): "
                                    + "%s %s",
                            bindInetAddress,
                            bindPortRanges));
            this.context.sendReply(
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
                            "Error in creating the client facing UDP "
                                    + "socket"),
                    e);
            this.context.sendReply(
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
                            "Error in binding the client facing UDP "
                                    + "socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            clientFacingDatagramSock.close();
            return null;
        }
        return clientFacingDatagramSock;
    }

    private DatagramSocket newRemoteFacingDatagramSocket() {
        Host remoteFacingDatagramSocketBindHost = this.getRemoteFacingDatagramSocketBindHost();
        InetAddress bindInetAddress;
        try {
            bindInetAddress = remoteFacingDatagramSocketBindHost.toInetAddress();
        } catch (UnknownHostException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to bind the remote facing UDP socket to "
                                    + "the following host: %s",
                            remoteFacingDatagramSocketBindHost));
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        PortRanges bindPortRanges = this.getRemoteFacingDatagramSocketBindPortRanges();
        DatagramSocket remoteFacingDatagramSock = null;
        boolean remoteFacingDatagramSockBound = false;
        for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
             !remoteFacingDatagramSockBound && iterator.hasNext(); ) {
            PortRange bindPortRange = iterator.next();
            for (Iterator<Port> iter = bindPortRange.iterator();
                 !remoteFacingDatagramSockBound && iter.hasNext(); ) {
                Port bindPort = iter.next();
                try {
                    remoteFacingDatagramSock =
                            this.newRemoteFacingDatagramSocket(
                                    bindInetAddress, bindPort);
                } catch (BindException e) {
                    continue;
                }
                if (remoteFacingDatagramSock == null) {
                    return null;
                }
                remoteFacingDatagramSockBound = true;
            }
        }
        if (!remoteFacingDatagramSockBound) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to bind the remote facing UDP socket to the "
                                    + "following address and port (range(s)): %s %s",
                            bindInetAddress,
                            bindPortRanges));
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        return remoteFacingDatagramSock;
    }

    private DatagramSocket newRemoteFacingDatagramSocket(
            final InetAddress bindInetAddress,
            final Port bindPort) throws BindException {
        DatagramSocketFactory remoteFacingDatagramSocketFactory =
                this.context.getSelectedRoute().getDatagramSocketFactory();
        DatagramSocket remoteFacingDatagramSock;
        try {
            remoteFacingDatagramSock =
                    remoteFacingDatagramSocketFactory.newDatagramSocket(null);
        } catch (SocketException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in creating the remote facing UDP "
                                    + "socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        if (!this.configureRemoteFacingDatagramSocket(
                remoteFacingDatagramSock)) {
            remoteFacingDatagramSock.close();
            return null;
        }
        try {
            remoteFacingDatagramSock.bind(new InetSocketAddress(
                    bindInetAddress, bindPort.intValue()));
        } catch (BindException e) {
            remoteFacingDatagramSock.close();
            throw e;
        } catch (SocketException e) {
            if (ThrowableHelper.isOrHasInstanceOf(e, BindException.class)) {
                remoteFacingDatagramSock.close();
                throw new BindException();
            }
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in binding the remote facing UDP "
                                    + "socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            remoteFacingDatagramSock.close();
            return null;
        }
        return remoteFacingDatagramSock;
    }

    private void passPackets(
            final UdpRelayServer udpRelayServer) throws IOException {
        try {
            udpRelayServer.start();
            try {
                while (this.context.getClientSocket().getInputStream().read() != -1) {
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
                this.context +
                "]";
    }

    private DatagramSocket wrapClientFacingDatagramSocket(
            final DatagramSocket clientFacingDatagramSock) {
        DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
        DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory =
                this.context.getClientFacingDtlsDatagramSocketFactory();
        try {
            clientFacingDatagramSck =
                    clientFacingDtlsDatagramSocketFactory.getDatagramSocket(
                            clientFacingDatagramSck);
        } catch (IOException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in wrapping the client facing UDP socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        try {
            clientFacingDatagramSck =
                    this.context.getMethodSubNegotiationResults().getDatagramSocket(
                            clientFacingDatagramSck);
        } catch (IOException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in wrapping the client facing UDP socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        return clientFacingDatagramSck;
    }

}
