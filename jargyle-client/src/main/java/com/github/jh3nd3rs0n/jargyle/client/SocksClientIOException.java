package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;

/**
 * Thrown when an I/O error occurs from the {@code SocksClient}.
 */
public class SocksClientIOException extends IOException {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@code SocksClient}.
     */
    private final SocksClient socksClient;

    /**
     * Constructs a {@code SocksClientIOException} with the provided
     * {@code SocksClient} and the provided message.
     *
     * @param client  the provided {@code SocksClient}
     * @param message the provided message
     */
    public SocksClientIOException(
            final SocksClient client, final String message) {
        super(getMessage(client, message, null));
        this.socksClient = client;
    }

    /**
     * Constructs a {@code SocksClientIOException} with the provided
     * {@code SocksClient} and the provided cause.
     *
     * @param client the provided {@code SocksClient}
     * @param cause  the provided cause
     */
    public SocksClientIOException(
            final SocksClient client, final Throwable cause) {
        super(getMessage(client, null, cause), cause);
        this.socksClient = client;
    }

    /**
     * Returns a message based on the provided {@code SocksClient}, the
     * optionally provided message, and the optionally provided cause.
     *
     * @param client  the provided {@code SocksClient}
     * @param message the optionally provided message (can be {@code null})
     * @param cause   the optionally provided cause (can be {@code null})
     * @return a message based on the provided {@code SocksClient}, the
     * optionally provided message, and the optionally provided cause
     */
    static String getMessage(
            final SocksClient client,
            final String message,
            final Throwable cause) {
        StringBuilder sb = new StringBuilder(String.format("from %s", client));
        if (message != null) {
            sb.append(": ");
            sb.append(message);
        }
        if (cause != null) {
            sb.append(": ");
            sb.append(cause);
        }
        return sb.toString();
    }

    /**
     * Returns the {@code SocksClient}.
     *
     * @return the {@code SocksClient}
     */
    public SocksClient getSocksClient() {
        return this.socksClient;
    }

}
