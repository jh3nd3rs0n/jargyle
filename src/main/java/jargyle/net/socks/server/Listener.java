package jargyle.net.socks.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.internal.logging.LoggerHelper;
import jargyle.internal.net.ssl.SslSocketFactory;
import jargyle.net.NetObjectFactory;
import jargyle.net.ssl.DtlsDatagramSocketFactory;
import jargyle.util.Criteria;
import jargyle.util.Criterion;

final class Listener implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			Listener.class);
	
	private Optional<DtlsDatagramSocketFactory> clientDtlsDatagramSocketFactory;
	private Optional<SslSocketFactory> clientSslSocketFactory;
	private final Configuration configuration;
	private final NetObjectFactory netObjectFactory;
	private final ServerSocket serverSocket;
			
	public Listener(final ServerSocket serverSock, final Configuration config) {
		this.clientDtlsDatagramSocketFactory = Optional.empty();
		this.clientSslSocketFactory = Optional.empty();		
		this.configuration = config;
		this.netObjectFactory = new NetObjectFactoryImpl(config);
		this.serverSocket = serverSock;
	}
	
	private boolean canAllowClientSocket(final Socket clientSocket) {
		Settings settings = this.configuration.getSettings();
		String clientAddress = 
				clientSocket.getInetAddress().getHostAddress();
		Criteria allowedClientAddressCriteria = settings.getLastValue(
				SettingSpec.ALLOWED_CLIENT_ADDRESS_CRITERIA);
		Criterion criterion = allowedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion == null) {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Client address %s not allowed",
					clientAddress)));
			return false;
		}
		Criteria blockedClientAddressCriteria = settings.getLastValue(
				SettingSpec.BLOCKED_CLIENT_ADDRESS_CRITERIA);
		criterion = blockedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion != null) {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Client address %s blocked based on the following "
					+ "criterion: %s",
					clientAddress,
					criterion)));
			return false;
		}
		return true;
	}
	
	private void closeClientSocket(final Socket clientSocket) {
		if (!clientSocket.isClosed()) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in closing the client socket"), 
						e);
			}
		}
	}
	
	private Optional<DtlsDatagramSocketFactory> getClientDtlsDatagramSocketFactory() {
		Settings settings = this.configuration.getSettings();
		if (settings.getLastValue(SettingSpec.DTLS_ENABLED).booleanValue()) {
			if (!this.clientDtlsDatagramSocketFactory.isPresent()) {
				this.clientDtlsDatagramSocketFactory = Optional.of(
						new DtlsDatagramSocketFactoryImpl(this.configuration));
			}
		} else {
			if (this.clientDtlsDatagramSocketFactory.isPresent()) {
				this.clientDtlsDatagramSocketFactory = Optional.empty();
			}
		}
		return this.clientDtlsDatagramSocketFactory;
	}
	
	private Optional<SslSocketFactory> getClientSslSocketFactory() {
		Settings settings = this.configuration.getSettings();
		if (settings.getLastValue(SettingSpec.SSL_ENABLED).booleanValue()) {
			if (!this.clientSslSocketFactory.isPresent()) {
				this.clientSslSocketFactory = Optional.of(
						new SslSocketFactoryImpl(this.configuration));
			}
		} else {
			if (this.clientSslSocketFactory.isPresent()) {
				this.clientSslSocketFactory = Optional.empty();
			}
		}
		return this.clientSslSocketFactory;
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
				LOGGER.warn(
						LoggerHelper.objectMessage(
								this, "Error in waiting for a connection"), 
						e);
				continue;
			}
			try {
				if (!this.canAllowClientSocket(clientSocket)) {
					this.closeClientSocket(clientSocket);
					continue;
				}
				Socket clientSock = this.wrapClientSocket(clientSocket); 
				if (clientSock == null) {
					this.closeClientSocket(clientSocket);
					continue; 
				}
				clientSocket = clientSock;
			} catch (Throwable t) {
				LOGGER.warn(
						LoggerHelper.objectMessage(this, "Internal server error"), 
						t);
				this.closeClientSocket(clientSocket);
				continue;
			}
			WorkerContext workerContext = new WorkerContext(
					clientSocket,
					this.configuration,
					this.netObjectFactory,
					this.getClientDtlsDatagramSocketFactory());
			executor.execute(new Worker(workerContext));
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
	
	private Socket wrapClientSocket(final Socket clientSocket) {
		Socket clientSock = clientSocket;
		Optional<SslSocketFactory> clientSslSockFactory = 
				this.getClientSslSocketFactory();
		if (clientSslSockFactory.isPresent()) {
			try {
				clientSock = clientSslSockFactory.get().newSocket(
						clientSock, null, true);
			} catch (IOException e) {
				LOGGER.warn(
						LoggerHelper.objectMessage(
								this, "Error in wrapping the client socket"), 
						e);
				return null;
			}
		}
		return clientSock;
	}

}
