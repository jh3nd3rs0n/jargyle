package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
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
 * A builder for connecting the SOCKS server {@code Socket}.
 */
public final class ConnectingSocksServerSocketBuilder {

    /**
     * The {@code HostResolverFactory} for this
     * {@code ConnectingSocksServerSocketBuilder}.
     */
    private final HostResolverFactory hostResolverFactory;

    /**
     * The {@code SocketFactory} for this
     * {@code ConnectingSocksServerSocketBuilder}.
     */
    private final SocketFactory socketFactory;

    /**
     * The {@code SocksClientAgent} for this
     * {@code ConnectingSocksServerSocketBuilder}.
     */
    private final SocksClientAgent socksClientAgent;

    /**
     * The SOCKS server {@code Socket} for this
     * {@code ConnectingSocksServerSocketBuilder}.
     */
    private final Socket socksServerSocket;

    /**
     * The {@code SslSocketFactory} for this
     * {@code ConnectingSocksServerSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * The timeout in milliseconds on waiting for the SOCKS server
     * {@code Socket} to connect.
     */
    private NonNegativeInteger connectTimeout;

    /**
     * The {@code boolean} value to indicate if this
     * {@code ConnectingSocksServerSocketBuilder} is to bind the
     * SOCKS server {@code Socket} before connecting.
     */
    private boolean toBind;

    /**
     * Constructs a {@code ConnectingSocksServerSocketBuilder} with the
     * provided SOCKS server {@code Socket}, the provided
     * {@code SocksClientAgent}, the provided {@code HostResolverFactory}, the
     * provided {@code SocketFactory}, and the provided
     * {@code SslSocketFactory}.
     *
     * @param socksServerSock    the provided SOCKS server {@code Socket}
     * @param agent              the provided {@code SocksClientAgent}
     * @param hstResolverFactory the provided {@code HostResolverFactory}
     * @param sockFactory        the provided {@code SocketFactory}
     * @param sslSockFactory     the provided {@code SslSocketFactory}
     */
    ConnectingSocksServerSocketBuilder(
            final Socket socksServerSock,
            final SocksClientAgent agent,
            final HostResolverFactory hstResolverFactory,
            final SocketFactory sockFactory,
            final SslSocketFactory sslSockFactory) {
        this.connectTimeout = agent.getSocksServerSocketConnectTimeout();
        this.hostResolverFactory = hstResolverFactory;
        this.socketFactory = sockFactory;
        this.socksClientAgent = agent;
        this.socksServerSocket = socksServerSock;
        this.sslSocketFactory = sslSockFactory;
        this.toBind = false;
    }

    /**
     * Returns the provided SOCKS server {@code Socket} connected to the
     * provided host {@code InetAddress} and the provided port of the SOCKS
     * server and bound based on the following properties:
     * {@code socksClient.socksServerSocket.bindHost},
     * {@code socksClient.socksServerSocket.bindHostAddressTypes},
     * {@code socksClient.socksServerSocket.bindPortRanges}, and
     * {@code socksClient.socksServerSocket.netInterface}.
     *
     * @param socksServerSock            the provided SOCKS server
     *                                   {@code Socket}
     * @param socksServerHostInetAddress the provided host
     *                                   {@code InetAddress} of the SOCKS
     *                                   server
     * @param socksServerPort            the provided port of the SOCKS server
     * @return the provided SOCKS server {@code Socket} connected to the
     * provided host {@code InetAddress} and the provided port of the SOCKS
     * server and bound based on the following properties:
     * {@code socksClient.socksServerSocket.bindHost},
     * {@code socksClient.socksServerSocket.bindHostAddressTypes},
     * {@code socksClient.socksServerSocket.bindPortRanges}, and
     * {@code socksClient.socksServerSocket.netInterface}
     * @throws IOException if an error occurs in binding or connecting the
     *                     provided SOCKS server {@code Socket}
     */
    private Socket getBoundConnectedSocksServerSocket(
            final Socket socksServerSock,
            final InetAddress socksServerHostInetAddress,
            final int socksServerPort) throws IOException {
        InetAddress localAddress =
                this.socksClientAgent.getSocksServerSocketBindHost().toInetAddress();
        PortRanges localPortRanges =
                this.socksClientAgent.getSocksServerSocketBindPortRanges();
        Socket socksServerSck = socksServerSock;
        boolean socksServerSockBound = false;
        for (Iterator<PortRange> iterator = localPortRanges.toList().iterator();
             !socksServerSockBound && iterator.hasNext(); ) {
            PortRange localPortRange = iterator.next();
            for (Iterator<Port> iter = localPortRange.iterator();
                 !socksServerSockBound && iter.hasNext(); ) {
                Port localPort = iter.next();
                try {
                    socksServerSck.bind(new InetSocketAddress(
                            localAddress,
                            localPort.intValue()));
                } catch (SocketException e) {
                    SocketSettings socketSettings =
                            SocketSettings.extractFrom(socksServerSck);
                    socksServerSck.close();
                    socksServerSck = this.socketFactory.newSocket();
                    socketSettings.applyTo(socksServerSck);
                    continue;
                }
                try {
                    socksServerSck.connect(new InetSocketAddress(
                                    socksServerHostInetAddress,
                                    socksServerPort),
                            this.connectTimeout.intValue());
                } catch (IOException e) {
                    if (ThrowableHelper.isOrHasInstanceOf(
                            e, BindException.class)) {
                        SocketSettings socketSettings =
                                SocketSettings.extractFrom(socksServerSck);
                        socksServerSck.close();
                        socksServerSck = this.socketFactory.newSocket();
                        socketSettings.applyTo(socksServerSck);
                        continue;
                    }
                    throw e;
                }
                socksServerSockBound = true;
            }
        }
        if (!socksServerSockBound) {
            throw new BindException(String.format(
                    "unable to bind to the following address and port "
                            + "(range(s)): %s %s",
                    localAddress,
                    localPortRanges));
        }
        return socksServerSck;
    }

    /**
     * Returns the connected SOCKS server {@code Socket}. If
     * {@link #setToBind(boolean)} is set to {@code true}, the {@code Socket}
     * to the SOCKS server will be bound based on the following properties:
     * {@code socksClient.socksServerSocket.bindHost},
     * {@code socksClient.socksServerSocket.bindHostAddressTypes},
     * {@code socksClient.socksServerSocket.bindPortRanges}, and
     * {@code socksClient.socksServerSocket.netInterface}. If the property
     * {@code socksClient.ssl.enabled} is set to {@code true}, a
     * {@code SSLSocket} layered over the connected SOCKS server
     * {@code Socket} is returned.
     *
     * @return the connected SOCKS server {@code Socket}
     * @throws IOException if there is an error in binding or connecting the
     *                     SOCKS server {@code Socket}
     */
    public Socket getConnectedSocksServerSocket() throws IOException {
        SocksServerUri socksServerUri =
                this.socksClientAgent.getSocksServerUri();
        String socksServerUriHost = socksServerUri.getHost().toString();
        int socksServerUriPort = socksServerUri.getPortOrDefault().intValue();
        HostResolver hostResolver = this.hostResolverFactory.newHostResolver();
        InetAddress socksServerUriHostInetAddress = hostResolver.resolve(
                socksServerUriHost);
        Socket socksServerSock = this.socksServerSocket;
        if (this.toBind) {
            socksServerSock = this.getBoundConnectedSocksServerSocket(
                    socksServerSock,
                    socksServerUriHostInetAddress,
                    socksServerUriPort);
        } else {
            socksServerSock.connect(
                    new InetSocketAddress(
                            socksServerUriHostInetAddress,
                            socksServerUriPort),
                    this.connectTimeout.intValue());
        }
        return this.sslSocketFactory.getSocket(
                socksServerSock,
                socksServerUriHost,
                socksServerUriPort,
                true);
    }

    /**
     * Sets the timeout in milliseconds on waiting for the SOCKS server
     * {@code Socket} to connect. A timeout of {@code 0} is interpreted as an
     * infinite timeout. An {@code IllegalArgumentException} is thrown if the
     * provided timeout is less than {@code 0}.
     *
     * @param i the provided timeout in milliseconds on waiting for the
     * SOCKS server {@code Socket} to connect
     * @return this {@code ConnectingSocksServerSocketBuilder}
     */
    public ConnectingSocksServerSocketBuilder setConnectTimeout(final int i) {
        this.connectTimeout = NonNegativeInteger.valueOf(i);
        return this;
    }

    /**
     * Sets the {@code boolean} value to indicate if this
     * {@code ConnectingSocksServerSocketBuilder} is to bind the
     * SOCKS server {@code Socket} before connecting.
     *
     * @param b the provided {@code boolean} value to indicate if this
     *          {@code ConnectingSocksServerSocketBuilder} is to bind the
     *          SOCKS server {@code Socket} before connecting
     * @return this {@code ConnectingSocksServerSocketBuilder}
     */
    public ConnectingSocksServerSocketBuilder setToBind(final boolean b) {
        this.toBind = b;
        return this;
    }

}
