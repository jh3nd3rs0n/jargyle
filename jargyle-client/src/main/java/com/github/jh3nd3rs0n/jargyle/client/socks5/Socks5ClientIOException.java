package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.SocksClientIOException;

public class Socks5ClientIOException extends SocksClientIOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Socks5ClientIOException(
			final Socks5Client client, final String message) {
		super(client, message);
	}

	public Socks5ClientIOException(
			final Socks5Client client, final Throwable cause) {
		super(client, cause);
	}

}
