package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Objects;

public final class ConnectClientSocketBuilder {

    private static final int DEFAULT_TIMEOUT = 60000;

    private final Socket clientSocket;
    private InetAddress localAddress;
    private PortRanges localPortRanges;
    private boolean mustBindBeforeConnect;
    private final NetObjectFactory netObjectFactory;
    private final SocksClient socksClient;
    private final SslSocketFactory sslSocketFactory;
    private int timeout;

    ConnectClientSocketBuilder(
            final SocksClient client,
            final Socket clientSock,
            final NetObjectFactory netObjFactory,
            final SslSocketFactory sslSockFactory) {
        this.clientSocket = clientSock;
        this.localAddress = HostIpv4Address.getAllZerosInet4Address();
        this.localPortRanges = PortRanges.getDefault();
        this.mustBindBeforeConnect = false;
        this.netObjectFactory = netObjFactory;
        this.socksClient = client;
        this.sslSocketFactory = sslSockFactory;
        this.timeout = DEFAULT_TIMEOUT;
    }

    private Socket getBoundConnectedClientSocket(
            final Socket clientSock,
            final InetAddress socksServerUriHostInetAddress,
            final int socksServerUriPort) throws IOException {
        NetObjectFactory netObjFactory = this.netObjectFactory;
        if (clientSock.getClass().equals(Socket.class)) {
            netObjFactory = NetObjectFactory.getDefault();
        }
        Socket clientSck = clientSock;
        boolean clientSockBound = false;
        for (Iterator<PortRange> iterator = this.localPortRanges.toList().iterator();
             !clientSockBound && iterator.hasNext(); ) {
            PortRange localPortRange = iterator.next();
            for (Iterator<Port> iter = localPortRange.iterator();
                 !clientSockBound && iter.hasNext(); ) {
                Port localPort = iter.next();
                try {
                    clientSck.bind(new InetSocketAddress(
                            this.localAddress,
                            localPort.intValue()));
                } catch (SocketException e) {
                    SocketSettings socketSettings =
                            SocketSettings.extractFrom(clientSck);
                    clientSck.close();
                    clientSck = netObjFactory.newSocket();
                    socketSettings.applyTo(clientSck);
                    continue;
                }
                try {
                    clientSck.connect(new InetSocketAddress(
                                    socksServerUriHostInetAddress,
                                    socksServerUriPort),
                            this.timeout);
                } catch (IOException e) {
                    if (ThrowableHelper.isOrHasInstanceOf(
                            e, BindException.class)) {
                        SocketSettings socketSettings =
                                SocketSettings.extractFrom(clientSck);
                        clientSck.close();
                        clientSck = netObjFactory.newSocket();
                        socketSettings.applyTo(clientSck);
                        continue;
                    }
                    throw e;
                }
                clientSockBound = true;
            }
        }
        if (!clientSockBound) {
            throw new BindException(String.format(
                    "unable to bind to the following address and port "
                            + "(range(s)): %s %s",
                    this.localAddress,
                    this.localPortRanges));
        }
        return clientSck;
    }

    public Socket getConnectedClientSocket() throws IOException {
        SocksServerUri socksServerUri = this.socksClient.getSocksServerUri();
        String socksServerUriHost = socksServerUri.getHost().toString();
        int socksServerUriPort = socksServerUri.getPortOrDefault().intValue();
        HostResolver hostResolver = this.netObjectFactory.newHostResolver();
        InetAddress socksServerUriHostInetAddress = hostResolver.resolve(
                socksServerUriHost);
        Socket clientSock = this.clientSocket;
        if (this.mustBindBeforeConnect) {
            clientSock = this.getBoundConnectedClientSocket(
                    clientSock,
                    socksServerUriHostInetAddress,
                    socksServerUriPort);
        } else {
            clientSock.connect(
                    new InetSocketAddress(
                            socksServerUriHostInetAddress,
                            socksServerUriPort),
                    this.timeout);
        }
        if (this.sslSocketFactory == null) {
            return clientSock;
        }
        clientSock = this.sslSocketFactory.newSocket(
                clientSock,
                socksServerUriHost,
                socksServerUriPort,
                true);
        return clientSock;
    }

    public ConnectClientSocketBuilder setLocalAddress(
            final InetAddress localAddr) {
        this.localAddress = Objects.requireNonNull(localAddr);
        this.mustBindBeforeConnect = true;
        return this;
    }

    public ConnectClientSocketBuilder setLocalPortRanges(
            final PortRanges localPrtRanges) {
        this.localPortRanges = Objects.requireNonNull(localPrtRanges);
        this.mustBindBeforeConnect = true;
        return this;
    }

    public ConnectClientSocketBuilder setTimeout(final int tOut) {
        this.timeout = tOut;
        return this;
    }

}
