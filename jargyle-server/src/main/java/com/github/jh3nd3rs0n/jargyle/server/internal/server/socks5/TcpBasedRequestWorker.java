package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Relay;

class TcpBasedRequestWorker extends RequestWorker {

	private static final int HALF_SECOND = 500;

	protected TcpBasedRequestWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubNegotiationResults methSubNegotiationResults, 
			final Request req) {
		super(socks5Worker, methSubNegotiationResults, req);
	}

	protected final void passData(final Relay relay) throws IOException {
		try {
			relay.start();
			while (!relay.getState().equals(Relay.State.STOPPED)) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} finally {
			if (!relay.getState().equals(Relay.State.STOPPED)) {
				relay.stop();
			}
		}		
	}	

}
