package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;

public final class FailureReplyException extends Socks5ClientIOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Reply failureReply;
	
	public FailureReplyException(
			final Socks5Client client, final Reply failureRep) {
		super(
				client, 
				String.format(
						"received failure reply: %s",
						failureRep));
		this.failureReply = failureRep;
	}

	public Reply getFailureReply() {
		return this.failureReply;
	}
	
}
