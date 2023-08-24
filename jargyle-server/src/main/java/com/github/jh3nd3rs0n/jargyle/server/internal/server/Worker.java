package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5.Socks5Worker;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5.Socks5WorkerContext;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Version;

final class Worker implements Runnable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Worker.class);
	
	private Socket clientSocket;
	private final AtomicInteger totalWorkerCount;
	private final WorkerContextFactory workerContextFactory;
	
	public Worker(
			final Socket clientSock, 
			final AtomicInteger workerCount,
			final WorkerContextFactory contextFactory) {
		this.clientSocket = clientSock;
		this.totalWorkerCount = workerCount;
		this.workerContextFactory = contextFactory;
	}
	
	private WorkerContext newWorkerContext(final Socket clientSock) {
		WorkerContext workerContext = null;
		try {
			workerContext = this.workerContextFactory.newWorkerContext(
					clientSock);
		} catch (IllegalArgumentException e) {
			LOGGER.debug(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Invalid client socket"), 
					e);			
			return null;
		} catch (UnsupportedOperationException e) {
			LOGGER.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the client socket"), 
					e);
			return null;			
		} catch (SocketException e) {
			LOGGER.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the client socket"), 
					e);
			return null;			
		} catch (IOException e) {
			LOGGER.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in wrapping the client socket"), 
					e);
			return null;
		}
		return workerContext;
	}
	
	public void run() {
		long startTime = System.currentTimeMillis();
		WorkerContext workerContext = null;
		try {
			LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Started. Total Worker count: %s",
					this.totalWorkerCount.incrementAndGet()));
			workerContext = this.newWorkerContext(this.clientSocket);
			if (workerContext == null) {
				return;
			}
			this.clientSocket = workerContext.getClientSocket();
			InputStream clientInputStream =	this.clientSocket.getInputStream();
			UnsignedByte version = null;
			try {
				version = UnsignedByte.newInstanceFrom(clientInputStream);
			} catch (IOException e) {
				ClientIOExceptionLoggingHelper.log(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
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
				LOGGER.error(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Unknown SOCKS version: %s",
						version.intValue()));
			}
		} catch (Throwable t) {
			LOGGER.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Internal server error"), 
					t);
		} finally {
			if (workerContext != null && !workerContext.isClosed()) {
				try {
					workerContext.close();
				} catch (IOException e) {
					LOGGER.error(
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error upon closing the worker context"), 
							e);
				}
			}
			if (!this.clientSocket.isClosed()) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					LOGGER.error(
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error upon closing connection to the "
									+ "client"), 
							e);
				}
			}
			LOGGER.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Finished in %s ms. Total Worker count: %s",
					System.currentTimeMillis() - startTime,
					this.totalWorkerCount.decrementAndGet()));
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
