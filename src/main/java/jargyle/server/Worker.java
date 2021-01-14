package jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.SocketInterface;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.Version;
import jargyle.common.util.Criteria;
import jargyle.common.util.Criterion;
import jargyle.server.socks5.Socks5Worker;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(
			Worker.class.getName());
	
	private final SocketInterface clientSocketInterface;
	private final Configuration configuration;
	private final Settings settings;
	
	public Worker(
			final SocketInterface clientSockInterface, 
			final Configuration config) {
		this.clientSocketInterface = clientSockInterface;
		this.configuration = config;
		this.settings = config.getSettings();
	}
	
	private boolean canAcceptClientSocketInterface(
			final SocketInterface clientSocketInterface) {
		String clientAddress = 
				clientSocketInterface.getInetAddress().getHostAddress();
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
	
	private boolean configureClientSocketInterface(
			final SocketInterface clientSocketInterface) {
		try {
			SocketSettings socketSettings =	this.settings.getLastValue(
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
		try {
			if (!this.canAcceptClientSocketInterface(
					this.clientSocketInterface)) {
				return;
			}
			if (!this.configureClientSocketInterface(
					this.clientSocketInterface)) {
				return;
			}
			InputStream clientInputStream = 
					this.clientSocketInterface.getInputStream();
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
						this.clientSocketInterface, 
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
			if (!this.clientSocketInterface.isClosed()) {
				try {
					this.clientSocketInterface.close();
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
			.append(" [clientSocketInterface=")
			.append(this.clientSocketInterface)
			.append("]");
		return builder.toString();
	}
	
}