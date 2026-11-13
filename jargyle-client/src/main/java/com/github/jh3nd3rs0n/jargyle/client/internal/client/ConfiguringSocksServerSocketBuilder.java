package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;

import java.net.Socket;
import java.net.SocketException;

/**
 * A builder for configuring the SOCKS server {@code Socket}.
 */
public final class ConfiguringSocksServerSocketBuilder {

    /**
     * The {@code HostResolverFactory} for this
     * {@code ConfiguringSocksServerSocketBuilder}.
     */
    private final HostResolverFactory hostResolverFactory;

    /**
     * The {@code SocketFactory} for this
     * {@code ConfiguringSocksServerSocketBuilder}.
     */
    private final SocketFactory socketFactory;

    /**
     * The {@code SocksClientAgent} for this
     * {@code ConfiguringSocksServerSocketBuilder}.
     */
    private final SocksClientAgent socksClientAgent;

    /**
     * The SOCKS server {@code Socket} for this
     * {@code ConfiguringSocksServerSocketBuilder}.
     */
    private final Socket socksServerSocket;

    /**
     * The {@code SslSocketFactory} for this
     * {@code ConfiguringSocksServerSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * Constructs a {@code ConfiguringSocksServerSocketBuilder} with the
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
    ConfiguringSocksServerSocketBuilder(
            final Socket socksServerSock,
            final SocksClientAgent agent,
            final HostResolverFactory hstResolverFactory,
            final SocketFactory sockFactory,
            final SslSocketFactory sslSockFactory) {
        this.hostResolverFactory = hstResolverFactory;
        this.socketFactory = sockFactory;
        this.socksClientAgent = agent;
        this.socksServerSocket = socksServerSock;
        this.sslSocketFactory = sslSockFactory;
    }

    /**
     * Configures the SOCKS server {@code Socket} from the property
     * {@code socksClient.socksServerSocket.socketSettings}.
     *
     * @return this {@code ConfiguringSocksServerSocketBuilder}
     * @throws SocketException if an error occurs in configuring the
     *                         SOCKS server {@code Socket}
     */
    public ConfiguringSocksServerSocketBuilder configure() throws SocketException {
        this.socksClientAgent.getSocksServerSocketSocketSettings().applyTo(
                this.socksServerSocket);
        return this;
    }

    /**
     * Proceeds to connect the SOCKS server {@code Socket} by deferring to a
     * returning new {@code ConnectingSocksServerSocketBuilder} to handle
     * connecting the SOCKS server {@code Socket}.
     *
     * @return a new {@code ConnectingSocksServerSocketBuilder} to handle
     * connecting the SOCKS server {@code Socket}
     */
    public ConnectingSocksServerSocketBuilder proceedToConnect() {
        return new ConnectingSocksServerSocketBuilder(
                this.socksServerSocket,
                this.socksClientAgent,
                this.hostResolverFactory,
                this.socketFactory,
                this.sslSocketFactory);
    }

}
