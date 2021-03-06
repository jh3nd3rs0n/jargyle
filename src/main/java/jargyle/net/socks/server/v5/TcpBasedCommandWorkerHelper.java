package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.Socket;

import jargyle.internal.net.socks.server.TcpRelayServer;

final class TcpBasedCommandWorkerHelper {

	private static final int HALF_SECOND = 500;

	public static void passData(
			final Socket clientFacingSocket,
			final Socket serverFacingSocket, 
			final int bufferSize, 
			final int timeout) throws IOException {
		TcpRelayServer tcpRelayServer = new TcpRelayServer(
				clientFacingSocket, 
				serverFacingSocket, 
				bufferSize, 
				timeout);
		try {
			tcpRelayServer.start();
			while (!tcpRelayServer.isStopped()) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} finally {
			if (!tcpRelayServer.isStopped()) {
				tcpRelayServer.stop();
			}
		}		
	}	
	
	private TcpBasedCommandWorkerHelper() { }
	
}
