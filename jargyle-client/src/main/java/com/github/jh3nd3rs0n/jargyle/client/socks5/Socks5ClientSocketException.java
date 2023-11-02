package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.SocksClientSocketException;

public final class Socks5ClientSocketException extends SocksClientSocketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Socks5ClientSocketException(
			final Socks5Client client, final Throwable cause) {
		super(client, cause);
	}

}
