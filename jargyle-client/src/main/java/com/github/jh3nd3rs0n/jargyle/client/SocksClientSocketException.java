package com.github.jh3nd3rs0n.jargyle.client;

import java.net.SocketException;

/**
 * Thrown when an error occurs in creating or accessing a Socket that has an
 * underlying {@code SocksClient}. This {@code SocketException} is used to
 * help determine from a chain of {@code SocksClient}s which
 * {@code SocksClient} is the associated error coming from.
 */
public final class SocksClientSocketException extends SocketException {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The cause.
     */
    private final Throwable cause;

    /**
     * The underlying {@code SocksClient}
     */
    private final SocksClient socksClient;

    /**
     * Constructs a {@code SocksClientSocketException} with the provided
     * underlying {@code SocksClient} and the provided cause.
     *
     * @param client the provided underlying {@code SocksClient}
     * @param c      the provided cause
     */
    public SocksClientSocketException(
            final SocksClient client, final Throwable c) {
        super(SocksClientIOException.getMessage(client, null, c));
        this.cause = c;
        this.socksClient = client;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

    /**
     * Returns the underlying {@code SocksClient}.
     *
     * @return the underlying {@code SocksClient}
     */
    public SocksClient getSocksClient() {
        return this.socksClient;
    }

}
