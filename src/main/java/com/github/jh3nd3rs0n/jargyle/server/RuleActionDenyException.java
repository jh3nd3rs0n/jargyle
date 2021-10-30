package com.github.jh3nd3rs0n.jargyle.server;

public final class RuleActionDenyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RuleActionDenyException(final String message) {
		super(message);
	}

}
