package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;

public class SocksClientException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String getMessage(
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

	public SocksClientException(
			final SocksClient client, final String message) {
		super(getMessage(client, message, null));
		this.socksClient = client;
	}

	public SocksClientException(
			final SocksClient client, final Throwable cause) {
		super(getMessage(client, null, cause), cause);
		this.socksClient = client;
	}

	public SocksClient getSocksClient() {
		return this.socksClient;
	}
	
}
