package jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.SocketSettings;
import jargyle.server.socks5.Socks5Worker;

final class Worker implements Runnable {
	
	private final Socket clientSocket;
	private final Configuration configuration;
	private final Logger logger;
	
	public Worker(
			final Socket clientSock, 
			final Configuration config, 
			final Logger lggr) {
		this.clientSocket = clientSock;
		this.configuration = config;
		this.logger = lggr;
	}
	
	private void close() {
		if (!this.clientSocket.isClosed()) {
			try {
				this.clientSocket.close();
			} catch (IOException e) {
				this.log(
						Level.WARNING, 
						"Error upon closing connection to the client", 
						e);
			}
		}
	}
	
	private void log(final Level level, final String message) {
		this.logger.log(
				level, 
				String.format("%s: %s", this, message));
	}
	
	private void log(
			final Level level, final String message, final Throwable t) {
		this.logger.log(
				level, 
				String.format("%s: %s", this, message), 
				t);
	}
	
	public void run() {
		try {
			String clientName = 
					this.clientSocket.getLocalAddress().getHostName();
			String clientAddress = 
					this.clientSocket.getLocalAddress().getHostAddress();
			Expressions allowedClientAddresses = 
					this.configuration.getAllowedClientAddresses();
			if (allowedClientAddresses.toList().isEmpty()) {
				allowedClientAddresses = Expressions.newInstance(
						ExpressionType.REGULAR.newExpression(".*"));
			}
			if (!allowedClientAddresses.anyMatches(clientName) 
					&& !allowedClientAddresses.anyMatches(clientAddress)) {
				this.log(
						Level.FINE, 
						String.format(
								"Client address not allowed: %s", 
								clientName));
				this.close();
				return;
			}
			Expressions blockedClientAddresses =
					this.configuration.getBlockedClientAddresses();
			if (blockedClientAddresses.toList().isEmpty()) {
				blockedClientAddresses = Expressions.newInstance(
						ExpressionType.REGULAR.newExpression("[^\\w\\W]"));
			}
			if (blockedClientAddresses.anyMatches(clientName)) {
				this.log(
						Level.FINE, 
						String.format(
								"Client address blocked: %s", 
								clientName));
				this.close();
				return;
			}
			if (blockedClientAddresses.anyMatches(clientAddress)) {
				this.log(
						Level.FINE, 
						String.format(
								"Client address blocked: %s", 
								clientAddress));
				this.close();
				return;
			}
			try {
				SocketSettings socketSettings = 
						this.configuration.getSettings().getLastValue(
								SettingSpec.CLIENT_SOCKET_SETTINGS,
								SocketSettings.class);
				socketSettings.applyTo(this.clientSocket);
			} catch (SocketException e) {
				this.log(
						Level.WARNING, 
						"Error in setting the client socket", 
						e);
				this.close();
				return;
			}
			InputStream clientInputStream = null;
			try {
				clientInputStream = this.clientSocket.getInputStream();
			} catch (IOException e) {
				this.log(
						Level.WARNING, 
						"Error in getting the input stream from the client", 
						e);
				this.close();
				return;
			}
			int version = -1;
			try {
				version = clientInputStream.read();
			} catch (IOException e) {
				this.log(
						Level.WARNING, 
						"Error in getting the SOCKS version from the client", 
						e);
				this.close();
				return;
			}
			if (version == -1) { 
				this.close(); 
				return; 
			}
			if ((byte) version == jargyle.common.net.socks5.Version.V5.byteValue()) {
				Socks5Worker socks5Worker = new Socks5Worker(
						this.clientSocket, 
						this.configuration, 
						this.logger);
				socks5Worker.run();
			} else {
				this.log(
						Level.WARNING,
						String.format(
								"Unknown SOCKS version: %s", 
								version));
				this.close();
			}
		} catch (Throwable t) {
			this.log(
					Level.WARNING, 
					"Internal server error", 
					t);
			this.close();
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