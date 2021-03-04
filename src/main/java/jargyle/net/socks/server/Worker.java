package jargyle.net.socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.net.NetFactory;
import jargyle.net.socks.server.v5.Socks5Worker;
import jargyle.net.socks.transport.v5.Version;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
	
	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private final Socket clientSocket;
	private final Configuration configuration;
	private final NetFactory externalNetFactory;
	
	public Worker(
			final Socket clientSock, 
			final Configuration config, 
			final NetFactory extNetFactory, 
			final DtlsDatagramSocketFactory clientDtlsDatagramSockFactory) {
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;		
		this.clientSocket = clientSock;
		this.configuration = config;
		this.externalNetFactory = extNetFactory;
	}
	
	private String format(final String message) {
		return String.format("%s: %s", this, message);
	}
	
	public void run() {
		try {
			InputStream clientInputStream = 
					this.clientSocket.getInputStream();
			int version = -1;
			try {
				version = clientInputStream.read();
			} catch (IOException e) {
				LOGGER.warn(
						this.format("Error in getting the SOCKS version from "
								+ "the client"), 
						e);
				return;
			}
			if ((byte) version == Version.V5.byteValue()) {
				Socks5Worker socks5Worker = new Socks5Worker(
						this.clientSocket, 
						this.configuration, 
						this.externalNetFactory, 
						this.clientDtlsDatagramSocketFactory);
				socks5Worker.run();
			} else {
				LOGGER.warn(this.format(String.format(
						"Unknown SOCKS version: %s",
						version)));
			}
		} catch (Throwable t) {
			LOGGER.warn(
					this.format("Internal server error"), 
					t);
		} finally {
			if (!this.clientSocket.isClosed()) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					LOGGER.warn(
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
