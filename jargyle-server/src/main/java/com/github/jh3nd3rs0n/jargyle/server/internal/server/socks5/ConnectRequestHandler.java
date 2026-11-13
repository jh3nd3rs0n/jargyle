package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
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

    private boolean canPrepareRemoteSocket() {
        return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketPrepareSocketFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private boolean configureRemoteSocket(
            final Socket remoteSocket) {
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

    private Host getRemoteSocketBindHost() {
        return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindHostFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private PortRanges getRemoteSocketBindPortRanges() {
        return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketBindPortRangesFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    private int getRemoteSocketConnectTimeout() {
        return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketConnectTimeoutFrom(
                        this.context.getApplicableRule(),
                        this.context.getSettings())
                .intValue();
    }

    private SocketSettings getRemoteSocketSocketSettings() {
        return Socks5ValueDerivationHelper.getSocks5OnConnectRequestRemoteSocketSocketSettingsFrom(
                this.context.getApplicableRule(),
                this.context.getSettings());
    }

    @Override
    public void handleRequest() throws IOException {
        Socket remoteSocket = null;
        Socket clientSocket = this.context.getClientSocket();
        Reply rep;
        try {
            remoteSocket = this.newRemoteSocket();
            if (remoteSocket == null) {
                return;
            }
            String serverBoundAddress =
                    remoteSocket.getInetAddress().getHostAddress();
            int serverBoundPort = remoteSocket.getPort();
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
            remoteSocket = this.limitRemoteSocket(
                    remoteSocket);
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

    private Socket limitRemoteSocket(final Socket remoteSocket) {
        Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
        if (inboundBandwidthLimit != null) {
            return new BandwidthLimitedSocket(
                    remoteSocket, inboundBandwidthLimit);
        }
        return remoteSocket;
    }

    private Socket newExtemporaneousRemoteSocket(
            final InetAddress bindInetAddress,
            final Port bindPort) throws BindException {
        SocketFactory remoteSocketFactory =
                this.context.getSelectedRoute().getSocketFactory();
        Socket remoteSocket;
        try {
            remoteSocket = remoteSocketFactory.newSocket(
                    this.context.getDesiredDestinationAddress().toString(),
                    this.context.getDesiredDestinationPort().intValue(),
                    bindInetAddress,
                    bindPort.intValue());
        } catch (UnknownHostException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in creating the remote socket"),
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
                                "Error in creating the remote socket"),
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
                                "Error in connecting the remote socket"),
                        e);
                this.context.sendReply(
                        Reply.newFailureInstance(ReplyCode.NETWORK_UNREACHABLE));
                return null;
            }
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in connecting the remote socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        if (!this.configureRemoteSocket(remoteSocket)) {
            try {
                remoteSocket.close();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            return null;
        }
        return remoteSocket;
    }

    private Socket newExtemporaneousRemoteSocket(
            final InetAddress bindInetAddress,
            final PortRanges bindPortRanges) {
        Socket remoteSocket = null;
        boolean remoteSocketBound = false;
        for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
             !remoteSocketBound && iterator.hasNext(); ) {
            PortRange bindPortRange = iterator.next();
            for (Iterator<Port> iter = bindPortRange.iterator();
                 !remoteSocketBound && iter.hasNext(); ) {
                Port bindPort = iter.next();
                try {
                    remoteSocket =
                            this.newExtemporaneousRemoteSocket(
                                    bindInetAddress, bindPort);
                } catch (BindException e) {
                    continue;
                }
                if (remoteSocket == null) {
                    return null;
                }
                remoteSocketBound = true;
            }
        }
        if (!remoteSocketBound) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to bind the remote socket to the "
                                    + "following address and port (range(s)): %s %s",
                            bindInetAddress,
                            bindPortRanges));
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        return remoteSocket;
    }

    private Socket newPreparedRemoteSocket(
            final InetAddress bindInetAddress,
            final Port bindPort) throws SocketException {
        InetAddress desiredDestinationInetAddress =
                this.resolveDesiredDestinationAddress(
                        this.context.getDesiredDestinationAddress().toString());
        if (desiredDestinationInetAddress == null) {
            return null;
        }
        SocketFactory remoteSocketFactory =
                this.context.getSelectedRoute().getSocketFactory();
        Socket remoteSocket;
        int connectTimeout = this.getRemoteSocketConnectTimeout();
        remoteSocket = remoteSocketFactory.newSocket();
        if (!this.configureRemoteSocket(remoteSocket)) {
            try {
                remoteSocket.close();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            return null;
        }
        try {
            remoteSocket.bind(new InetSocketAddress(
                    bindInetAddress, bindPort.intValue()));
        } catch (SocketException e) {
            try {
                remoteSocket.close();
            } catch (IOException ex) {
                throw new AssertionError(ex);
            }
            throw e;
        } catch (IOException e) {
            if (ThrowableHelper.isOrHasInstanceOf(e, SocketException.class)) {
                try {
                    remoteSocket.close();
                } catch (IOException ex) {
                    throw new AssertionError(ex);
                }
                throw new SocketException();
            }
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in binding the remote socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            try {
                remoteSocket.close();
            } catch (IOException ex) {
                throw new AssertionError(ex);
            }
            return null;
        }
        try {
            remoteSocket.connect(new InetSocketAddress(
                            desiredDestinationInetAddress,
                            this.context.getDesiredDestinationPort().intValue()),
                    connectTimeout);
        } catch (IOException e) {
            if (ThrowableHelper.isOrHasInstanceOf(
                    e, BindException.class)) {
                try {
                    remoteSocket.close();
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
                                "Error in connecting the remote socket"),
                        e);
                this.context.sendReply(
                        Reply.newFailureInstance(
                                ReplyCode.NETWORK_UNREACHABLE));
                try {
                    remoteSocket.close();
                } catch (IOException ex) {
                    throw new AssertionError(ex);
                }
                return null;
            }
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in connecting the remote socket"),
                    e);
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            try {
                remoteSocket.close();
            } catch (IOException ex) {
                throw new AssertionError(ex);
            }
            return null;
        }
        return remoteSocket;
    }

    private Socket newPreparedRemoteSocket(
            final InetAddress bindInetAddress,
            final PortRanges bindPortRanges) {
        Socket remoteSocket = null;
        boolean remoteSocketBound = false;
        for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
             !remoteSocketBound && iterator.hasNext(); ) {
            PortRange bindPortRange = iterator.next();
            for (Iterator<Port> iter = bindPortRange.iterator();
                 !remoteSocketBound && iter.hasNext(); ) {
                Port bindPort = iter.next();
                try {
                    remoteSocket = this.newPreparedRemoteSocket(
                            bindInetAddress, bindPort);
                } catch (SocketException e) {
                    continue;
                }
                if (remoteSocket == null) {
                    return null;
                }
                remoteSocketBound = true;
            }
        }
        if (!remoteSocketBound) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to bind the remote socket to the "
                                    + "following address and port (range(s)): %s %s",
                            bindInetAddress,
                            bindPortRanges));
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        return remoteSocket;
    }

    private Socket newRemoteSocket() {
        Host remoteSocketBindHost = this.getRemoteSocketBindHost();
        InetAddress bindInetAddress;
        try {
            bindInetAddress = remoteSocketBindHost.toInetAddress();
        } catch (UnknownHostException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Unable to bind the remote socket to the "
                                    + "following host: %s",
                            remoteSocketBindHost));
            this.context.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return null;
        }
        PortRanges bindPortRanges = this.getRemoteSocketBindPortRanges();
        return this.canPrepareRemoteSocket() ?
                this.newPreparedRemoteSocket(
                        bindInetAddress, bindPortRanges)
                : this.newExtemporaneousRemoteSocket(
                bindInetAddress, bindPortRanges);
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
                                    + "address for the remote socket: %s",
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
                                        + "address for the remote socket: %s",
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
                                    + "address for the remote socket: %s",
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
