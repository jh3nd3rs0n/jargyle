package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.SocksException;

public class Socks5Exception extends SocksException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Socks5Exception(final String message) {
		super(message);
	}

	public Socks5Exception(final Throwable cause) {
		super(cause);
	}

	public Socks5Exception(final String message, final Throwable cause) {
		super(message, cause);
	}

}
