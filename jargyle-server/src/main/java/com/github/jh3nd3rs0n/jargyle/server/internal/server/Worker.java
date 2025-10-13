package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Worker implements Runnable {

    private final WorkerContext context;
    private final Logger logger;

	public Worker(final WorkerContext cntxt) {
        this.context = cntxt;
		this.logger = LoggerFactory.getLogger(Worker.class);
	}

	public void run() {
		long startTime = System.currentTimeMillis();
        this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
                this,
                "Started. Current Worker count: %s",
                this.context.incrementAndGetCurrentWorkerCount()));
		try {
            ConnectionHandler connectionHandler = new ConnectionHandler(
                    new ConnectionHandlerContext(
                            this.context,
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
					this.context.decrementAndGetCurrentWorkerCount()));
		}
	}

	@Override
	public String toString() {
        return this.getClass().getSimpleName() +
                " [context=" +
                this.context +
                "]";
	}

}
