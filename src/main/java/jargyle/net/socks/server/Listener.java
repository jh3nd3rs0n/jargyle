package jargyle.net.socks.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.logging.LoggerHelper;
import jargyle.net.NetObjectFactory;
import jargyle.net.ssl.DtlsDatagramSocketFactory;
import jargyle.net.ssl.SslSocketFactory;
import jargyle.util.Criteria;
import jargyle.util.Criterion;

final class Listener implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			Listener.class);
	
	private DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private SslSocketFactory clientSslSocketFactory;
	private final Configuration configuration;
	private final NetObjectFactory netObjectFactory;
	private final ServerSocket serverSocket;
			
	public Listener(final ServerSocket serverSock, final Configuration config) {
		this.clientDtlsDatagramSocketFactory = null;
		this.clientSslSocketFactory = null;		
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
	
	private DtlsDatagramSocketFactory getClientDtlsDatagramSocketFactory() {
		if (!this.configuration.getSettings().getLastValue(
				SettingSpec.DTLS_ENABLED).booleanValue()) {
			return null;
		}
		if (this.clientDtlsDatagramSocketFactory == null) {
			this.clientDtlsDatagramSocketFactory = 
					new DtlsDatagramSocketFactoryImpl(this.configuration);
		}
		return this.clientDtlsDatagramSocketFactory;
	}
	
	private SslSocketFactory getClientSslSocketFactory() {
		if (!this.configuration.getSettings().getLastValue(
				SettingSpec.SSL_ENABLED).booleanValue()) {
			return null;
		}
		if (this.clientSslSocketFactory == null) {
			this.clientSslSocketFactory = new SslSocketFactoryImpl(
					this.configuration);
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
		SslSocketFactory clientSslSockFactory = 
				this.getClientSslSocketFactory();
		if (clientSslSockFactory != null) {
			try {
				clientSock = clientSslSockFactory.newSocket(
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
