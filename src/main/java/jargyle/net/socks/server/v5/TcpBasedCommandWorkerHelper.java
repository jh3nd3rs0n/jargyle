package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.Socket;

import jargyle.net.RelayServer;

final class TcpBasedCommandWorkerHelper {

	private static final int HALF_SECOND = 500;

	public static void passData(
			final Socket clientFacingSocket,
			final Socket serverFacingSocket, 
			final int bufferSize, 
			final int timeout) throws IOException {
		RelayServer relayServer = new RelayServer(
				clientFacingSocket, 
				serverFacingSocket, 
				bufferSize, 
				timeout);
		try {
			relayServer.start();
			while (!relayServer.isStopped()) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} finally {
			if (!relayServer.isStopped()) {
				relayServer.stop();
			}
		}		
	}	
	
	private TcpBasedCommandWorkerHelper() { }
	
}
