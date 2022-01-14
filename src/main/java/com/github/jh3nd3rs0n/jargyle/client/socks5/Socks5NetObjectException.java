package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.SocksNetObjectException;

public final class Socks5NetObjectException extends SocksNetObjectException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Socks5NetObjectException(final String message) {
		super(message);
	}

	public Socks5NetObjectException(final Throwable cause) {
		super(cause);
	}

	public Socks5NetObjectException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
