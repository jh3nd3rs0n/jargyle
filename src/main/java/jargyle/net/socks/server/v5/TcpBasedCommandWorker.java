package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.Socket;

import jargyle.net.socks.server.TcpRelayServer;

abstract class TcpBasedCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;
	
	public TcpBasedCommandWorker(final CommandWorkerContext context) {
		super(context);
	}
	
	protected void passData(
			final Socket clientSocket,
			final Socket serverSocket, 
			final int bufferSize, 
			final int timeout) throws IOException {
		TcpRelayServer tcpRelayServer = new TcpRelayServer(
				clientSocket, 
				serverSocket, 
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
