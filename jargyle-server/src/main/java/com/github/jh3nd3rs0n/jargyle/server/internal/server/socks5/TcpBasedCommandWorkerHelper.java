package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;

import com.github.jh3nd3rs0n.jargyle.server.internal.server.RelayServer;

final class TcpBasedCommandWorkerHelper {

	private static final int HALF_SECOND = 500;

	public static void passData(
			final RelayServer.Builder builder) throws IOException {
		RelayServer relayServer = builder.build();
		try {
			relayServer.start();
			while (!relayServer.getState().equals(RelayServer.State.STOPPED)) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} finally {
			if (!relayServer.getState().equals(RelayServer.State.STOPPED)) {
				relayServer.stop();
			}
		}		
	}	
	
	private TcpBasedCommandWorkerHelper() { }
	
}
