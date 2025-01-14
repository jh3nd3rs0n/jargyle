package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;

/**
 * A builder for connecting the client socket to the SOCKS server.
 */
public final class ConnectingClientSocketBuilder {

    /**
     * The client socket for this {@code ConnectingClientSocketBuilder}.
     */
    private final Socket clientSocket;

    /**
     * The {@code NetObjectFactory} for this
     * {@code ConnectingClientSocketBuilder}.
     */
    private final NetObjectFactory netObjectFactory;

    /**
     * The {@code Properties} for this {@code ConnectingClientSocketBuilder}.
     */
    private final Properties properties;

    /**
     * The {@code SocksServerUri} for this
     * {@code ConnectingClientSocketBuilder}.
     */
    private final SocksServerUri socksServerUri;

    /**
     * The {@code SslSocketFactory} for this
     * {@code ConnectingClientSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * The timeout in milliseconds on waiting for the client socket to connect
     * to the SOCKS server.
     */
    private NonNegativeInteger connectTimeout;

    /**
     * The {@code boolean} value to indicate if this
     * {@code ConnectingClientSocketBuilder} is to bind the client socket.
     */
    private boolean toBind;

    /**
     * Constructs a {@code ConnectingClientSocketBuilder} with the provided
     * client socket, the provided {@code SocksServerUri}, the provided
     * {@code Properties}, the provided {@code NetObjectFactory}, and the
     * provided {@code SslSocketFactory}.
     *
     * @param clientSock     the provided client socket
     * @param serverUri      the provided {@code SocksServerUri}
     * @param props          the provided {@code Properties}
     * @param netObjFactory  the provided {@code NetObjectFactory}
     * @param sslSockFactory the provided {@code SslSocketFactory}
     */
    ConnectingClientSocketBuilder(
            final Socket clientSock,
            final SocksServerUri serverUri,
            final Properties props,
            final NetObjectFactory netObjFactory,
            final SslSocketFactory sslSockFactory) {
        this.clientSocket = clientSock;
        this.connectTimeout = props.getValue(
                GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT);
        this.netObjectFactory = netObjFactory;
        this.properties = props;
        this.socksServerUri = serverUri;
        this.sslSocketFactory = sslSockFactory;
        this.toBind = false;
    }

    /**
     * Returns the provided client socket connected to the provided host
     * {@code InetAddress} and the provided port of the SOCKS server and bound
     * based on the following properties: {@code socksClient.clientBindHost},
     * {@code socksClient.clientBindHostAddressTypes},
     * {@code socksClient.clientBindPortRanges}, and
     * {@code socksClient.clientNetInterface}.
     *
     * @param clientSock                 the provided client socket
     * @param socksServerHostInetAddress the provided host
     *                                   {@code InetAddress} of the SOCKS
     *                                   server
     * @param socksServerPort            the provided port of the SOCKS server
     * @return the provided client socket connected to the provided host
     * {@code InetAddress} and the provided port of the SOCKS server and bound
     * based on the following properties: {@code socksClient.clientBindHost},
     * {@code socksClient.clientBindHostAddressTypes},
     * {@code socksClient.clientBindPortRanges}, and
     * {@code socksClient.clientNetInterface}
     * @throws IOException if an error occurs in binding or connecting the
     *                     client socket
     */
    private Socket getBoundConnectedClientSocket(
            final Socket clientSock,
            final InetAddress socksServerHostInetAddress,
            final int socksServerPort) throws IOException {
        InetAddress localAddress =
                GeneralValueDerivationHelper.getClientBindHostFrom(
                        this.properties).toInetAddress();
        PortRanges localPortRanges = this.properties.getValue(
                GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES);
        Socket clientSck = clientSock;
        boolean clientSockBound = false;
        for (Iterator<PortRange> iterator = localPortRanges.toList().iterator();
             !clientSockBound && iterator.hasNext(); ) {
            PortRange localPortRange = iterator.next();
            for (Iterator<Port> iter = localPortRange.iterator();
                 !clientSockBound && iter.hasNext(); ) {
                Port localPort = iter.next();
                try {
                    clientSck.bind(new InetSocketAddress(
                            localAddress,
                            localPort.intValue()));
                } catch (SocketException e) {
                    SocketSettings socketSettings =
                            SocketSettings.extractFrom(clientSck);
                    clientSck.close();
                    clientSck = this.netObjectFactory.newSocket();
                    socketSettings.applyTo(clientSck);
                    continue;
                }
                try {
                    clientSck.connect(new InetSocketAddress(
                                    socksServerHostInetAddress,
                                    socksServerPort),
                            this.connectTimeout.intValue());
                } catch (IOException e) {
                    if (ThrowableHelper.isOrHasInstanceOf(
                            e, BindException.class)) {
                        SocketSettings socketSettings =
                                SocketSettings.extractFrom(clientSck);
                        clientSck.close();
                        clientSck = this.netObjectFactory.newSocket();
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
                    localAddress,
                    localPortRanges));
        }
        return clientSck;
    }

    /**
     * Returns the client socket connected to the SOCKS server. If
     * {@link #setToBind(boolean)} is set to {@code true}, the client socket
     * will be bound based on the following properties:
     * {@code socksClient.clientBindHost},
     * {@code socksClient.clientBindHostAddressTypes},
     * {@code socksClient.clientBindPortRanges}, and
     * {@code socksClient.clientNetInterface}. If the property
     * {@code socksClient.ssl.enabled} is set to {@code true}, a client
     * {@code SSLSocket} layered over the client socket connected to the SOCKS
     * server is returned.
     *
     * @return the client socket connected to the SOCKS server
     * @throws IOException if there is an error in binding or connecting the
     *                     client socket
     */
    public Socket getConnectedClientSocket() throws IOException {
        String socksServerUriHost = this.socksServerUri.getHost().toString();
        int socksServerUriPort =
                this.socksServerUri.getPortOrDefault().intValue();
        HostResolver hostResolver = this.netObjectFactory.newHostResolver();
        InetAddress socksServerUriHostInetAddress = hostResolver.resolve(
                socksServerUriHost);
        Socket clientSock = this.clientSocket;
        if (this.toBind) {
            clientSock = this.getBoundConnectedClientSocket(
                    clientSock,
                    socksServerUriHostInetAddress,
                    socksServerUriPort);
        } else {
            clientSock.connect(
                    new InetSocketAddress(
                            socksServerUriHostInetAddress,
                            socksServerUriPort),
                    this.connectTimeout.intValue());
        }
        return this.sslSocketFactory.getSocket(
                clientSock,
                socksServerUriHost,
                socksServerUriPort,
                true);
    }

    /**
     * Sets the timeout in milliseconds on waiting for the client socket to
     * connect to the SOCKS server. A timeout of {@code 0} is interpreted as
     * an infinite timeout. An {@code IllegalArgumentException} is thrown if
     * the provided timeout is less than {@code 0}.
     *
     * @param i the provided timeout in milliseconds on waiting for the client
     *          socket to connect to the SOCKS server
     * @return this {@code ConnectingClientSocketBuilder}
     */
    public ConnectingClientSocketBuilder setConnectTimeout(final int i) {
        this.connectTimeout = NonNegativeInteger.valueOf(i);
        return this;
    }

    /**
     * Sets the {@code boolean} value to indicate if this
     * {@code ConnectingClientSocketBuilder} is to bind the client socket.
     *
     * @param b the provided {@code boolean} value to indicate if this
     *          {@code ConnectingClientSocketBuilder} is to bind the client socket
     * @return this {@code ConnectingClientSocketBuilder}
     */
    public ConnectingClientSocketBuilder setToBind(final boolean b) {
        this.toBind = b;
        return this;
    }

}
