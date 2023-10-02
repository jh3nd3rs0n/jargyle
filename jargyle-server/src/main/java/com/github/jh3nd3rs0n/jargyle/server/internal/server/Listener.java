package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

public final class Listener implements Runnable {

	private final ConfiguredWorkerParamsProvider configuredWorkerParamsProvider;
	private final AtomicInteger currentWorkerCount;	
	private final Logger logger;
	private final ServerSocket serverSocket;
			
	public Listener(final ServerSocket serverSock, final Configuration config) {
		this.configuredWorkerParamsProvider = 
				new ConfiguredWorkerParamsProvider(config);		
		this.currentWorkerCount = new AtomicInteger(0);
		this.logger = LoggerFactory.getLogger(Listener.class);
		this.serverSocket = serverSock;
	}
	
	public void run() {
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			while (true) {
				try {
					Socket clientSocket = this.serverSocket.accept();
					executor.execute(new Worker(
							clientSocket,
							this.currentWorkerCount,
							this.configuredWorkerParamsProvider));
				} catch (SocketTimeoutException e) {
					this.logger.error(
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Timeout reached in waiting for a "
									+ "connection!"), 
							e);
					continue;				
				} catch (SocketException e) {
					// closed by SocksServer.stop()
					break;
				} catch (IOException e) {
					this.logger.error(
							ObjectLogMessageHelper.objectLogMessage(
									this, "Error in waiting for a connection"), 
							e);
					continue;
				}
			}
		} finally {
			executor.shutdownNow();
		}
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
	
}
