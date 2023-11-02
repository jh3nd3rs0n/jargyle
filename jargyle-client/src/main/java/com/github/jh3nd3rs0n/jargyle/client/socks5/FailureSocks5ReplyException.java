package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

public final class FailureSocks5ReplyException extends Socks5ClientIOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Socks5Reply failureSocks5Reply;
	
	public FailureSocks5ReplyException(
			final Socks5Client client, final Socks5Reply failureSocks5Rep) {
		super(
				client, 
				String.format(
						"received failure SOCKS5 reply: %s", 
						failureSocks5Rep));
		this.failureSocks5Reply = failureSocks5Rep;
	}

	public Socks5Reply getFailureSocks5Reply() {
		return this.failureSocks5Reply;
	}
	
}
