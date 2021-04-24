package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.Socket;

import jargyle.net.socks.server.TcpRelayServer;

abstract class PassDataCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;
	
	public PassDataCommandWorker(final CommandWorkerContext context) {
		super(context);
	}
	
	protected void passData(
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

}
