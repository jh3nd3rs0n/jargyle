package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.server.internal.server.RelayServer;

class TcpBasedCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	protected TcpBasedCommandWorker(
			final Socket clientSocket, final CommandWorkerContext context) {
		super(clientSocket, context);
	}

	protected final void passData(
			final RelayServer relayServer) throws IOException {
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

}
