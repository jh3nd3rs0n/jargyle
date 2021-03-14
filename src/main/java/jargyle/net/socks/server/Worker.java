package jargyle.net.socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.net.socks.server.v5.Socks5Worker;
import jargyle.net.socks.transport.v5.Version;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
	
	private final Socket clientSocket;
	private final WorkerParams workerParams;
	
	public Worker(final WorkerParams params) {
		this.clientSocket = params.getClientSocket();
		this.workerParams = params;
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
				Socks5Worker socks5Worker = new Socks5Worker(this.workerParams);
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
