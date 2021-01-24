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
import jargyle.common.net.SocketSettings;
import jargyle.common.util.Criteria;
import jargyle.common.util.Criterion;

final class Listener implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(
			Listener.class.getName());

	private final ChainingAgentService chainingAgentService;	
	private final Configuration configuration;
	private final ServerSocket serverSocket;
	private final SslWrapper sslWrapper;
	
	public Listener(final ServerSocket serverSock, final Configuration config) {
		this.chainingAgentService = new ChainingAgentService(config);		
		this.configuration = config;
		this.serverSocket = serverSock;
		this.sslWrapper = new SslWrapper(config);
	}
	
	private boolean canAcceptClientSocketInterface(
			final SocketInterface clientSocketInterface) {
		Settings settings = this.configuration.getSettings();
		String clientAddress = 
				clientSocketInterface.getInetAddress().getHostAddress();
		Criteria allowedClientAddressCriteria = settings.getLastValue(
				SettingSpec.ALLOWED_CLIENT_ADDRESS_CRITERIA, Criteria.class);
		Criterion criterion = allowedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion == null) {
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Client address %s not allowed", 
							clientAddress)));
			return false;
		}
		Criteria blockedClientAddressCriteria = settings.getLastValue(
				SettingSpec.BLOCKED_CLIENT_ADDRESS_CRITERIA, Criteria.class);
		criterion = blockedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion != null) {
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Client address %s blocked based on the "
							+ "following criterion: %s", 
							clientAddress,
							criterion)));
			return false;
		}
		return true;
	}
	
	private void closeClientSocketInterface(
			final SocketInterface clientSocketInterface) {
		if (!clientSocketInterface.isClosed()) {
			try {
				clientSocketInterface.close();
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in closing the client socket"), 
						e);
			}
		}
	}
	
	private boolean configureClientSocketInterface(
			final SocketInterface clientSocketInterface) {
		Settings settings = this.configuration.getSettings();
		try {
			SocketSettings socketSettings =	settings.getLastValue(
					SettingSpec.CLIENT_SOCKET_SETTINGS, SocketSettings.class);
			socketSettings.applyTo(clientSocketInterface);
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the client socket"), 
					e);
			return false;
		}
		return true;
	}
	
	private String format(final String message) {
		return String.format("%s: %s", this, message);
	}
	
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
						this.format("Error in waiting for a connection"), 
						e);
				continue;
			}
			SocketInterface clientSocketInterface = new DirectSocketInterface(
					clientSocket);
			try {
				if (!this.canAcceptClientSocketInterface(
						clientSocketInterface)) {
					this.closeClientSocketInterface(clientSocketInterface);
					continue;
				}
				if (!this.configureClientSocketInterface(
						clientSocketInterface)) {
					this.closeClientSocketInterface(clientSocketInterface);
					continue;
				}
				SocketInterface clientSockInterface = 
						this.wrapClientSocketInterface(clientSocketInterface); 
				if (clientSockInterface == null) {
					this.closeClientSocketInterface(clientSocketInterface);
					continue; 
				}
				clientSocketInterface = clientSockInterface;
			} catch (Throwable t) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Internal server error"), 
						t);
				this.closeClientSocketInterface(clientSocketInterface);
				continue;
			}
			executor.execute(new Worker(
					clientSocketInterface, 
					this.configuration, 
					this.sslWrapper, 
					this.chainingAgentService));
		}
		executor.shutdownNow();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [serverSocket=")
			.append(this.serverSocket)
			.append("]");
		return builder.toString();
	}
	
	private SocketInterface wrapClientSocketInterface(
			final SocketInterface clientSocketInterface) {
		SocketInterface clientSockInterface = null;
		try {
			clientSockInterface = this.sslWrapper.wrapIfSslEnabled(
					clientSocketInterface, 
					null, 
					true);
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in wrapping the client socket"), 
					e);
			return null;
		}
		return clientSockInterface;
	}

}
