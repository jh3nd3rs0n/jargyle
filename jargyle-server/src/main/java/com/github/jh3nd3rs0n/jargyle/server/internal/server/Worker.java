package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

final class Worker implements Runnable {
	
	private final Socket clientSocket;
    private final ConfiguredObjectsProvider configuredObjectsProvider;
	private final AtomicInteger currentWorkerCount;
    private final Logger logger;

	public Worker(
			final Socket clientSock, 
			final AtomicInteger workerCount,
			final ConfiguredObjectsProvider configuredObjsProvider) {
		this.clientSocket = clientSock;
        this.configuredObjectsProvider = configuredObjsProvider;
		this.currentWorkerCount = workerCount;		
		this.logger = LoggerFactory.getLogger(Worker.class);
	}

	public void run() {
		long startTime = System.currentTimeMillis();
        this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                this,
                "Started. Current Worker count: %s",
                this.currentWorkerCount.incrementAndGet()));
		try {
            ConnectionHandler connectionHandler = new ConnectionHandler(
                    new ConnectionHandlerContext(
                            this.clientSocket,
                            this.configuredObjectsProvider.getConfiguredObjects(),
                            ServerEventLogger.newInstance(ConnectionHandler.class)));
            connectionHandler.handleConnection();
		} catch (Throwable t) {
			this.logger.warn(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Internal server error"), 
					t);
		} finally {
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Finished in %s ms. Current Worker count: %s",
					System.currentTimeMillis() - startTime,
					this.currentWorkerCount.decrementAndGet()));
		}
	}

	@Override
	public String toString() {
        return this.getClass().getSimpleName() +
                " [clientSocket=" +
                this.clientSocket +
                "]";
	}

}
