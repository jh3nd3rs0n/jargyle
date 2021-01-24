package jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.SocketInterface;
import jargyle.common.net.socks5.Version;
import jargyle.server.socks5.Socks5Worker;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger(
			Worker.class.getName());
	
	private final SocketInterface clientSocketInterface;
	private final Configuration configuration;
	private final Router router;
	private final SslWrapper sslWrapper;
	
	public Worker(
			final SocketInterface clientSockInterface, 
			final Configuration config, 
			final SslWrapper wrapper, 
			final Router rtr) {
		this.clientSocketInterface = clientSockInterface;
		this.configuration = config;
		this.router = rtr;		
		this.sslWrapper = wrapper;
	}
	
	private String format(final String message) {
		return String.format("%s: %s", this, message);
	}
	
	public void run() {
		try {
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
						this.configuration, 
						this.sslWrapper, 
						this.router);
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