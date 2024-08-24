package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.SocksException;

/**
 * Thrown when an exception occurs during the SOCKS5 protocol.
 */
public class Socks5Exception extends SocksException {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a {@code Socks5Exception}.
	 */
	public Socks5Exception() {
	}

	/**
	 * Constructs a {@code Socks5Exception} with the provided message.
	 *
	 * @param message the provided message
	 */
	public Socks5Exception(final String message) {
		super(message);
	}

	/**
	 * Constructs a {@code Socks5Exception} with the provided cause.
	 *
	 * @param cause the provided cause
	 */
	public Socks5Exception(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a {@code Socks5Exception} with the provided message and the
	 * provided cause.
	 *
	 * @param message the provided message
	 * @param cause   the provided cause
	 */
	public Socks5Exception(final String message, final Throwable cause) {
		super(message, cause);
	}

}
