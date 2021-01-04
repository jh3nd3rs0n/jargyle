package jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.DirectSocketInterface;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.Version;
import jargyle.server.socks5.Socks5Worker;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(
			Worker.class.getName());
	
	private final Socket clientSocket;
	private final Configuration configuration;
	private final Settings settings;
	
	public Worker(
			final Socket clientSock, 
			final Configuration config) {
		this.clientSocket = clientSock;
		this.configuration = config;
		this.settings = config.getSettings();
	}
	
	private boolean canAcceptClientSocket(final Socket clientSocket) {
		String clientAddress = clientSocket.getInetAddress().getHostAddress();
		Criteria allowedClientAddressCriteria = this.settings.getLastValue(
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
		Criteria blockedClientAddressCriteria = this.settings.getLastValue(
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
	
	private boolean configureClientSocket(final Socket clientSocket) {
		try {
			SocketSettings socketSettings =	this.settings.getLastValue(
					SettingSpec.CLIENT_SOCKET_SETTINGS, SocketSettings.class);
			socketSettings.applyTo(new DirectSocketInterface(clientSocket));
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
			InputStream clientInputStream = this.clientSocket.getInputStream();
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
			if ((byte) version == Version.V5.byteValue()) {
				Socks5Worker socks5Worker = new Socks5Worker(
						new DirectSocketInterface(this.clientSocket), 
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