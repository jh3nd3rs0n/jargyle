package com.github.jh3nd3rs0n.jargyle.transport.socks5;

public final class MethodSubnegotiationException extends Socks5Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MethodSubnegotiationException(final String message) {
		super(message);
	}
	
	public MethodSubnegotiationException(final Throwable cause) {
		super(cause);
	}
	
}
