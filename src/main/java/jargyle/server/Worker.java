package jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.Version;
import jargyle.server.socks5.Socks5Worker;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(
			Worker.class.getName());
	
	private final Socket clientSocket;
	private final Configuration configuration;
	
	public Worker(
			final Socket clientSock, 
			final Configuration config) {
		this.clientSocket = clientSock;
		this.configuration = config;
	}
	
	private boolean canAcceptClientSocket(final Socket clientSocket) {
		InetAddress clientInetAddress = clientSocket.getInetAddress();
		Criteria allowedClientAddressCriteria = 
				this.configuration.getAllowedClientAddressCriteria();
		if (allowedClientAddressCriteria.toList().isEmpty()) {
			allowedClientAddressCriteria = Criteria.newInstance(
					CriterionOperator.MATCHES.newCriterion(".*"));
		}
		Criterion criterion = allowedClientAddressCriteria.anyEvaluatesTrue(
				clientInetAddress);
		if (criterion == null) {
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Client address %s not allowed", 
							clientInetAddress)));
			return false;
		}
		Criteria blockedClientAddressCriteria =
				this.configuration.getBlockedClientAddressCriteria();
		criterion = blockedClientAddressCriteria.anyEvaluatesTrue(
				clientInetAddress);
		if (criterion != null) {
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Client address %s blocked based on the "
							+ "following criterion: %s", 
							clientInetAddress,
							criterion)));
			return false;
		}
		return true;
	}
	
	private boolean configureClientSocket(final Socket clientSocket) {
		try {
			SocketSettings socketSettings = 
					this.configuration.getSettings().getLastValue(
							SettingSpec.CLIENT_SOCKET_SETTINGS,
							SocketSettings.class);
			socketSettings.applyTo(clientSocket);
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
		try {
			if (!this.canAcceptClientSocket(this.clientSocket)) {
				return;
			}
			if (!this.configureClientSocket(this.clientSocket)) {
				return;
			}
			InputStream clientInputStream = null;
			try {
				clientInputStream = this.clientSocket.getInputStream();
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in getting the input stream from "
								+ "the client"), 
						e);
				return;
			}
			int version = -1;
			try {
				version = clientInputStream.read();
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in getting the SOCKS version from "
								+ "the client"), 
						e);
				return;
			}
			if (version == -1) { return; }
			if ((byte) version == Version.V5.byteValue()) {
				Socks5Worker socks5Worker = new Socks5Worker(
						this.clientSocket, 
						this.configuration);
				socks5Worker.run();
			} else {
				LOGGER.log(
						Level.WARNING,
						this.format(String.format(
								"Unknown SOCKS version: %s", 
								version)));
			}
		} catch (Throwable t) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Internal server error"), 
					t);
		} finally {
			if (!this.clientSocket.isClosed()) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error upon closing connection to the "
									+ "client"), 
							e);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientSocket=")
			.append(this.clientSocket)
			.append("]");
		return builder.toString();
	}
	
}