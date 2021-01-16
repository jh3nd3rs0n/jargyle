package jargyle.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.DirectSocketInterface;
import jargyle.common.net.SocketInterface;

final class Listener implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(
			Listener.class.getName());
	
	private final Configuration configuration;
	private final ServerSocket serverSocket;
	private final SslWrapper sslWrapper;
	
	public Listener(final ServerSocket serverSock, final Configuration config) {
		this.configuration = config;
		this.serverSocket = serverSock;
		this.sslWrapper = new SslWrapper(config);
	}
	
	@SuppressWarnings("resource")
	public void run() {
		ExecutorService executor = Executors.newCachedThreadPool();
		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (SocketException e) {
				// closed by SocksServer.stop()
				break;
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						"Error in waiting for a connection", 
						e);
				continue;
			}
			SocketInterface clientSocketInterface = new DirectSocketInterface(
					clientSocket);
			try {
				clientSocketInterface = this.sslWrapper.wrapIfSslEnabled(
						clientSocketInterface, 
						null, 
						true);
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						"Error in wrapping the client socket", 
						e);
				if (!clientSocketInterface.isClosed()) {
					try {
						clientSocketInterface.close();
					} catch (IOException e1) {
						LOGGER.log(
								Level.WARNING, 
								"Error in closing the client socket", 
								e1);
					}
				}
				continue;
			}
			executor.execute(new Worker(
					clientSocketInterface, 
					this.configuration, 
					this.sslWrapper));
		}
		executor.shutdownNow();
	}

}
