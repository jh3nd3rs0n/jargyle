package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;

/**
 * A builder for the SOCKS server {@code Socket}.
 */
public final class SocksServerSocketBuilder {

    /**
     * The {@code HostResolverFactory} for this
     * {@code SocksServerSocketBuilder}.
     */
    private final HostResolverFactory hostResolverFactory;

    /**
     * The {@code SocketFactory} for this {@code SocksServerSocketBuilder}.
     */
    private final SocketFactory socketFactory;

    /**
     * The {@code SocksClientAgent} for this {@code SocksServerSocketBuilder}.
     */
    private final SocksClientAgent socksClientAgent;

    /**
     * The {@code SslSocketFactory} for this {@code SocksServerSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * Constructs a {@code SocksServerSocketBuilder} with the provided
     * {@code SocksClientAgent}, the provided {@code HostResolverFactory}, the
     * provided {@code SocketFactory}, and the provided
     * {@code SslSocketFactory}.
     *
     * @param agent              the provided {@code SocksClientAgent}
     * @param hstResolverFactory the provided {@code HostResolverFactory}
     * @param sockFactory        the provided {@code SocketFactory}
     * @param sslSockFactory     the provided {@code SslSocketFactory}
     */
    SocksServerSocketBuilder(
            final SocksClientAgent agent,
            final HostResolverFactory hstResolverFactory,
            final SocketFactory sockFactory,
            final SslSocketFactory sslSockFactory) {
        this.hostResolverFactory = hstResolverFactory;
        this.socketFactory = sockFactory;
        this.socksClientAgent = agent;
        this.sslSocketFactory = sslSockFactory;
    }

    /**
     * Returns a new bound and connected SOCKS server {@code Socket} based on
     * the properties {@code socksClient.socksServerSocket.bindHost},
     * {@code socksClient.socksServerSocket.bindHostAddressTypes},
     * {@code socksClient.socksServerSocket.bindPortRanges}, and
     * {@code socksClient.socksServerSocket.netInterface}. If the property
     * {@code socksClient.ssl.enabled} is set to {@code true}, a new bound and
     * connected SOCKS server {@code SSLSocket} is returned.
     *
     * @return a new bound and connected SOCKS server {@code Socket} based on
     * the properties
     * {@code socksClient.socksServerSocket.bindHost},
     * {@code socksClient.socksServerSocket.bindHostAddressTypes},
     * {@code socksClient.socksServerSocket.bindPortRanges}, and
     * {@code socksClient.socksServerSocket.netInterface}
     * @throws IOException if an I/O error occurs in creating the SOCKS server
     *                     socket
     */
    public Socket newBoundConnectedSocksServerSocket() throws IOException {
        InetAddress localAddr =
                this.socksClientAgent.getSocksServerSocketBindHost().toInetAddress();
        PortRanges localPortRanges =
                this.socksClientAgent.getSocksServerSocketBindPortRanges();
        Socket socksServerSocket = null;
        boolean socksServerSocketBound = false;
        for (Iterator<PortRange> iterator = localPortRanges.toList().iterator();
             !socksServerSocketBound && iterator.hasNext(); ) {
            PortRange localPortRange = iterator.next();
            for (Iterator<Port> iter = localPortRange.iterator();
                 !socksServerSocketBound && iter.hasNext(); ) {
                Port localPort = iter.next();
                try {
                    socksServerSocket = this.newBoundConnectedSocksServerSocket(
                            localAddr,
                            localPort.intValue());
                } catch (IOException e) {
                    if (ThrowableHelper.isOrHasInstanceOf(
                            e, BindException.class)) {
                        continue;
                    }
                    throw e;
                }
                socksServerSocketBound = true;
            }
        }
        if (!socksServerSocketBound) {
            throw new BindException(String.format(
                    "unable to bind to the following address and port "
                            + "(range(s)): %s %s",
                    localAddr,
                    localPortRanges));
        }
        return socksServerSocket;
    }

    /**
     * Returns a new connected SOCKS server {@code Socket} bound to the
     * provided local {@code InetAddress} and the provided local port. If the
     * property {@code socksClient.ssl.enabled} is set to {@code true}, a new
     * bound and connected SOCKS server {@code SSLSocket} is returned.
     *
     * @param localAddr the provided local {@code InetAddress}
     * @param localPort the provided local port
     * @return a new connected SOCKS server {@code Socket} bound to the
     * provided local {@code InetAddress} and the provided local port
     * @throws IOException if an I/O error occurs in creating the SOCKS server
     *                     {@code Socket}
     */
    public Socket newBoundConnectedSocksServerSocket(
            final InetAddress localAddr,
            final int localPort) throws IOException {
        SocksServerUri socksServerUri =
                this.socksClientAgent.getSocksServerUri();
        String socksServerUriHost = socksServerUri.getHost().toString();
        int socksServerUriPort =
                socksServerUri.getPortOrDefault().intValue();
        Socket socksServerSocket = this.socketFactory.newSocket(
                socksServerUriHost,
                socksServerUriPort,
                localAddr,
                localPort);
        return this.sslSocketFactory.getSocket(
                socksServerSocket,
                socksServerUriHost,
                socksServerUriPort,
                true);
    }

    /**
     * Returns a new unbound and unconnected SOCKS server {@code Socket}.
     *
     * @return a new unbound and unconnected SOCKS server {@code Socket}
     */
    public Socket newSocksServerSocket() {
        return this.socketFactory.newSocket();
    }

    /**
     * Proceeds to configure the provided SOCKS server {@code Socket} by
     * deferring to a returning new
     * {@code ConfiguringSocksServerSocketBuilder} to handle configuring the
     * provided SOCKS server {@code Socket}.
     *
     * @param socksServerSocket the provided SOCKS server {@code Socket}
     * @return a new {@code ConfiguringSocksServerSocketBuilder} to handle
     * configuring the provided SOCKS server {@code Socket}
     */
    public ConfiguringSocksServerSocketBuilder proceedToConfigure(
            final Socket socksServerSocket) {
        return new ConfiguringSocksServerSocketBuilder(
                socksServerSocket,
                this.socksClientAgent,
                this.hostResolverFactory,
                this.socketFactory,
                this.sslSocketFactory);
    }

}
