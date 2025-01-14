package com.github.jh3nd3rs0n.jargyle.client;

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
     * The {@code NetObjectFactory} for this
     * {@code ConfiguringClientSocketBuilder}.
     */
    private final NetObjectFactory netObjectFactory;

    /**
     * The {@code Properties} for this {@code ConfiguringClientSocketBuilder}.
     */
    private final Properties properties;

    /**
     * The {@code SocksServerUri} for this
     * {@code ConfiguringClientSocketBuilder}.
     */
    private final SocksServerUri socksServerUri;

    /**
     * The {@code SslSocketFactory} for this
     * {@code ConfiguringClientSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * Constructs a {@code ConfiguringClientSocketBuilder} with the provided
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
    ConfiguringClientSocketBuilder(
            final Socket clientSock,
            final SocksServerUri serverUri,
            final Properties props,
            final NetObjectFactory netObjFactory,
            final SslSocketFactory sslSockFactory) {
        this.clientSocket = clientSock;
        this.netObjectFactory = netObjFactory;
        this.properties = props;
        this.socksServerUri = serverUri;
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
        this.properties.getValue(
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
                this.socksServerUri,
                this.properties,
                this.netObjectFactory,
                this.sslSocketFactory);
    }

}
