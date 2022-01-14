package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;

public class SocksNetObjectException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SocksNetObjectException(final String message) {
		super(message);
	}

	public SocksNetObjectException(final Throwable cause) {
		super(cause);
	}

	public SocksNetObjectException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
