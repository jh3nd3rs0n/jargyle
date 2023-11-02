package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;

public class SocksClientIOException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	private final SocksClient socksClient;

	public SocksClientIOException(
			final SocksClient client, final String message) {
		super(getMessage(client, message, null));
		this.socksClient = client;
	}

	public SocksClientIOException(
			final SocksClient client, final Throwable cause) {
		super(getMessage(client, null, cause), cause);
		this.socksClient = client;
	}

	public SocksClient getSocksClient() {
		return this.socksClient;
	}
	
}
