package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * A factory that may create {@code SSLSocket}s. It is a wrapper of
 * {@link javax.net.ssl.SSLSocketFactory}.
 */
public abstract class SslSocketFactory {

    /**
     * Allows the construction of subclass instances.
     */
    public SslSocketFactory() {
    }

    /**
     * Returns a new default {@code SslSocketFactory} with the provided
     * {@code SSLContext}.
     *
     * @param sslContext the provided {@code SSLContext}
     * @return a new default {@code SslSocketFactory} with the provided
     * {@code SSLContext}
     */
    public static SslSocketFactory newInstance(final SSLContext sslContext) {
        return new DefaultSslSocketFactory(sslContext);
    }

    /**
     * Returns depending on the implementation either a new server mode
     * {@code Socket} layered over the existing connected {@code Socket} or
     * the existing connected {@code Socket}. This method is a wrapper of the
     * method
     * {@link javax.net.ssl.SSLSocketFactory#createSocket(Socket, InputStream, boolean)}.
     *
     * @param socket    the existing connected {@code Socket}
     * @param consumed  the consumed inbound network data that has already been
     *                  removed from the existing connected {@code Socket}'s
     *                  {@code InputStream}. This parameter may be {@code null}
     *                  if no data has been removed
     * @param autoClose close the underlying {@code Socket} when this
     *                  {@code Socket} is closed
     * @return depending on the implementation either a new server mode
     * {@code Socket} layered over the existing connected {@code Socket} or
     * the existing connected {@code Socket}
     * @throws IOException if an I/O error occurs when creating the new
     *                     {@code Socket}
     */
    public abstract Socket getSocket(
            final Socket socket,
            final InputStream consumed,
            final boolean autoClose) throws IOException;

    /**
     * Returns depending on the implementation either a new {@code Socket}
     * layered over the existing {@code Socket} connected to the named host
     * at the given port or the existing {@code Socket}. This method is a
     * wrapper of the method
     * {@link javax.net.ssl.SSLSocketFactory#createSocket(Socket, String, int, boolean)}.
     *
     * @param socket    the existing connected {@code Socket}
     * @param host      the server host
     * @param port      the server port
     * @param autoClose close the underlying {@code Socket} when this
     *                  {@code Socket} is closed
     * @return depending on the implementation either a new {@code Socket}
     * layered over the existing {@code Socket} connected to the named host
     * at the given port or the existing {@code Socket}
     * @throws IOException if an I/O error occurs when creating the
     *                     {@code Socket}
     */
    public abstract Socket getSocket(
            final Socket socket,
            final String host,
            final int port,
            final boolean autoClose) throws IOException;

}
