package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.common.net.RelayServer;

final class TcpBasedCommandWorkerHelper {

	private static final int HALF_SECOND = 500;

	public static void passData(
			final Socket clientFacingSocket,
			final Socket serverFacingSocket, 
			final int bufferSize, 
			final int idleTimeout) throws IOException {
		RelayServer relayServer = new RelayServer(
				clientFacingSocket, 
				serverFacingSocket, 
				bufferSize, 
				idleTimeout);
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
