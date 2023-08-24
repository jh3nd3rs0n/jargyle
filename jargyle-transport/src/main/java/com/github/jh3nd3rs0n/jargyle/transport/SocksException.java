package com.github.jh3nd3rs0n.jargyle.transport;

import java.io.IOException;

public class SocksException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SocksException(final String message) {
		super(message);
	}

	public SocksException(final Throwable cause) {
		super(cause);
	}

	public SocksException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
