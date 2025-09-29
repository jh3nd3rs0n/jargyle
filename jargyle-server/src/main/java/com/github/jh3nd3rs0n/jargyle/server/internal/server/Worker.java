package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

final class Worker implements Runnable {
	
    private final Logger logger;
    private final WorkerContext workerContext;

	public Worker(final WorkerContext context) {
		this.logger = LoggerFactory.getLogger(Worker.class);
        this.workerContext = context;
	}

	public void run() {
		long startTime = System.currentTimeMillis();
        this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                this,
                "Started. Current Worker count: %s",
                this.workerContext.incrementAndGetCurrentWorkerCount()));
		try {
            ConnectionHandler connectionHandler = new ConnectionHandler(
                    new ConnectionHandlerContext(
                            this.workerContext,
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
					this.workerContext.decrementAndGetCurrentWorkerCount()));
		}
	}

	@Override
	public String toString() {
        return this.getClass().getSimpleName() +
                " [workerContext=" +
                this.workerContext +
                "]";
	}

}
