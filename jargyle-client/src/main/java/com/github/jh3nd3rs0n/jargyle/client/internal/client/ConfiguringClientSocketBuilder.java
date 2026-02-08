package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;

import java.net.Socket;
import java.net.SocketException;

/**
 * A builder for configuring the client socket.
 */
public final class ConfiguringClientSocketBuilder {

    /**
     * The client socket for this {@code ConfiguringClientSocketBuilder}.
     */
    private final Socket clientSocket;

    /**
     * The {@code HostResolverFactory} for this
     * {@code ConfiguringClientSocketBuilder}.
     */
    private final HostResolverFactory hostResolverFactory;

    /**
     * The {@code SocketFactory} for this
     * {@code ConfiguringClientSocketBuilder}.
     */
    private final SocketFactory socketFactory;

    /**
     * The {@code SocksClientAgent} for this
     * {@code ConfiguringClientSocketBuilder}.
     */
    private final SocksClientAgent socksClientAgent;

    /**
     * The {@code SslSocketFactory} for this
     * {@code ConfiguringClientSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * Constructs a {@code ConfiguringClientSocketBuilder} with the provided
     * client socket, the provided {@code SocksClientAgent}, the provided
     * {@code HostResolverFactory}, the provided {@code SocketFactory}, and the
     * provided {@code SslSocketFactory}.
     *
     * @param clientSock         the provided client socket
     * @param agent              the provided {@code SocksClientAgent}
     * @param hstResolverFactory the provided {@code HostResolverFactory}
     * @param sockFactory        the provided {@code SocketFactory}
     * @param sslSockFactory     the provided {@code SslSocketFactory}
     */
    ConfiguringClientSocketBuilder(
            final Socket clientSock,
            final SocksClientAgent agent,
            final HostResolverFactory hstResolverFactory,
            final SocketFactory sockFactory,
            final SslSocketFactory sslSockFactory) {
        this.clientSocket = clientSock;
        this.hostResolverFactory = hstResolverFactory;
        this.socketFactory = sockFactory;
        this.socksClientAgent = agent;
        this.sslSocketFactory = sslSockFactory;
    }

    /**
     * Configures the client socket from the property
     * {@code socksClient.clientSocketSettings}.
     *
     * @return this {@code ConfiguringClientSocketBuilder}
     * @throws SocketException if an error occurs in configuring the client
     *                         socket
     */
    public ConfiguringClientSocketBuilder configure() throws SocketException {
        this.socksClientAgent.getProperties().getValue(
                GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS).applyTo(
                this.clientSocket);
        return this;
    }

    /**
     * Proceeds to connect the client socket to the SOCKS server by deferring
     * to a returning new {@code ConnectingClientSocketBuilder} to handle
     * connecting the client socket to the SOCKS server.
     *
     * @return a new {@code ConnectingClientSocketBuilder} to handle connecting
     * the client socket to the SOCKS server
     */
    public ConnectingClientSocketBuilder proceedToConnect() {
        return new ConnectingClientSocketBuilder(
                this.clientSocket,
                this.socksClientAgent,
                this.hostResolverFactory,
                this.socketFactory,
                this.sslSocketFactory);
    }

}
