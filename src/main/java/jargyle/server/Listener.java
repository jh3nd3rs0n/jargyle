package jargyle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

final class Listener implements Runnable {

	private final Configuration configuration;
	private final ServerSocket serverSocket;
	
	public Listener(
			final ServerSocket serverSock, 
			final Configuration config) {
		this.configuration = config;
		this.serverSocket = serverSock;
	}
	
	public void run() {
		ExecutorService executor = Executors.newCachedThreadPool();
		while (true) {
			try {
				Socket clientSocket = this.serverSocket.accept();
				executor.execute(new Worker(clientSocket, this.configuration));
			} catch (IOException e) {
				if (e instanceof SocketException) {
					break;
				}
				LoggerHolder.LOGGER.log(
						Level.WARNING, 
						"Error in waiting for a connection", 
						e);
			}
		}
		executor.shutdownNow();
	}

}
