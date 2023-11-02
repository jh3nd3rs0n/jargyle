package com.github.jh3nd3rs0n.jargyle.client;

import java.net.SocketException;

public final class SocksClientSocketException extends SocketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Throwable cause;
	private final SocksClient socksClient;
	
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
	
	public SocksClient getSocksClient() {
		return this.socksClient;
	}

}
