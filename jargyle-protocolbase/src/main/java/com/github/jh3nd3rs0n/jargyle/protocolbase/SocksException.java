package com.github.jh3nd3rs0n.jargyle.protocolbase;

import java.io.IOException;

/**
 * Thrown when an exception occurs during the SOCKS protocol.
 */
public class SocksException extends IOException {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code SocksException}.
     */
    public SocksException() {
    }

    /**
     * Constructs a {@code SocksException} with the provided message.
     *
     * @param message the provided message
     */
    public SocksException(final String message) {
        super(message);
    }

    /**
     * Constructs a {@code SocksException} with the provided cause.
     *
     * @param cause the provided cause
     */
    public SocksException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a {@code SocksException} with the provided message and the
     * provided cause.
     *
     * @param message the provided message
     * @param cause   the provided cause
     */
    public SocksException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
