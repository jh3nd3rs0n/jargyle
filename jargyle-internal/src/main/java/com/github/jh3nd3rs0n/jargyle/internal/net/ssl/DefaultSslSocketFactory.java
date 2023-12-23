package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Default implementation of {@code SslSocketFactory}.
 */
final class DefaultSslSocketFactory extends SslSocketFactory {

    /**
     * The {@code SSLContext}.
     */
    private final SSLContext sslContext;

    /**
     * Constructs a {@code DefaultSslSocketFactory} with the provided
     * {@code SSLContext}.
     *
     * @param sslCntxt the provided {@code SSLContext}
     */
    public DefaultSslSocketFactory(final SSLContext sslCntxt) {
        this.sslContext = Objects.requireNonNull(sslCntxt);
    }

    @Override
    public Socket newSocket(
            final Socket socket,
            final InputStream consumed,
            final boolean autoClose) throws IOException {
        SSLSocketFactory factory = this.sslContext.getSocketFactory();
        return factory.createSocket(socket, consumed, autoClose);
    }

    @Override
    public Socket newSocket(
            final Socket socket,
            final String host,
            final int port,
            final boolean autoClose) throws IOException {
        SSLSocketFactory factory = this.sslContext.getSocketFactory();
        return factory.createSocket(socket, host, port, autoClose);
    }

}
