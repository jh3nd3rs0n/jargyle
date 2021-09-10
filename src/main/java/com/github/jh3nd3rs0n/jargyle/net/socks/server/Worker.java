package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.Socks5Worker;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.Socks5WorkerContext;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Version;
import com.github.jh3nd3rs0n.jargyle.util.UnsignedByte;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
	
	private Socket clientFacingSocket;
	private final AtomicInteger totalWorkerCount;
	private final WorkerContextFactory workerContextFactory;
	
	public Worker(
			final Socket clientFacingSock, 
			final AtomicInteger workerCount,
			final WorkerContextFactory contextFactory) {
		this.clientFacingSocket = clientFacingSock;
		this.totalWorkerCount = workerCount;
		this.workerContextFactory = contextFactory;
	}
	
	private WorkerContext newWorkerContext(final Socket clientFacingSock) {
		WorkerContext workerContext = null;
		try {
			workerContext = this.workerContextFactory.newWorkerContext(
					clientFacingSock);
		} catch (IllegalArgumentException e) {
			LOGGER.warn(
					LoggerHelper.objectMessage(
							this, 
							String.format(
									"Client address %s is blocked or not allowed", 
									clientFacingSock.getInetAddress().getHostAddress())), 
					e);
			return null;
		} catch (SocketException e) {
			LOGGER.warn(
					LoggerHelper.objectMessage(
							this, "Error in setting the client-facing socket"), 
					e);
			return null;			
		} catch (IOException e) {
			LOGGER.warn(
					LoggerHelper.objectMessage(
							this, "Error in wrapping the client-facing socket"), 
					e);
			return null;
		}
		return workerContext;
	}
	
	public void run() {
		long startTime = System.currentTimeMillis();
		try {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Started. Total Worker count: %s",
					this.totalWorkerCount.incrementAndGet())));
			WorkerContext workerContext = this.newWorkerContext(
					this.clientFacingSocket);
			if (workerContext == null) {
				return;
			}
			this.clientFacingSocket = workerContext.getClientFacingSocket();
			InputStream clientFacingInputStream = 
					this.clientFacingSocket.getInputStream();
			UnsignedByte version = null;
			try {
				version = UnsignedByte.newInstanceFrom(clientFacingInputStream);
			} catch (IOException e) {
				LOGGER.warn(
						LoggerHelper.objectMessage(
								this, 
								"Error in getting the SOCKS version from the "
								+ "client"), 
						e);
				return;
			}			
			if (version.byteValue() == Version.V5.byteValue()) {
				Socks5Worker socks5Worker = new Socks5Worker(
						new Socks5WorkerContext(workerContext));
				socks5Worker.run();
			} else {
				LOGGER.warn(LoggerHelper.objectMessage(this, String.format(
						"Unknown SOCKS version: %s",
						version.intValue())));
			}
		} catch (Throwable t) {
			LOGGER.warn(
					LoggerHelper.objectMessage(this, "Internal server error"), 
					t);
		} finally {
			if (!this.clientFacingSocket.isClosed()) {
				try {
					this.clientFacingSocket.close();
				} catch (IOException e) {
					LOGGER.warn(
							LoggerHelper.objectMessage(
									this, 
									"Error upon closing connection to the "
									+ "client"), 
							e);
				}
			}
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Finished in %s ms. Total Worker count: %s",
					System.currentTimeMillis() - startTime,
					this.totalWorkerCount.decrementAndGet())));
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientFacingSocket=")
			.append(this.clientFacingSocket)
			.append("]");
		return builder.toString();
	}
	
}
