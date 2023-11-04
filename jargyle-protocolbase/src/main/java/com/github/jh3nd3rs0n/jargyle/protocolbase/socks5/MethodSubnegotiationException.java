package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

public final class MethodSubnegotiationException extends Socks5Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String getMessage(
			final Method method, final String message, final Throwable cause) {
		StringBuilder sb = new StringBuilder(String.format("method %s", method));
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
	
	private final Method method;
	
	public MethodSubnegotiationException(
			final Method meth, final String message) {
		super(getMessage(meth, message, null));
		this.method = meth;
	}
	
	public MethodSubnegotiationException(
			final Method meth, final Throwable cause) {
		super(getMessage(meth, null, cause), cause);
		this.method = meth;
	}
	
	public Method getMethod() {
		return this.method;
	}
	
}
