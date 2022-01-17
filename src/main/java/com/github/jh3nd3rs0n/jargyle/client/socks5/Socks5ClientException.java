package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.SocksClientException;

public class Socks5ClientException extends SocksClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Socks5ClientException(
			final Socks5Client client, final String message) {
		super(client, message);
	}

	public Socks5ClientException(
			final Socks5Client client, final Throwable cause) {
		super(client, cause);
	}

}
