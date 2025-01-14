package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public final class EchoDatagramServerLoadTestRunner {

    private static final int HALF_SECOND = 500;

    private final EchoDatagramServerInterface echoDatagramServerInterface;
    private final SocksServerInterface socksServerInterface;
    private final long delayBetweenThreadsStarting;
    private final EchoDatagramServerTestRunnerFactory echoDatagramServerTestRunnerFactory;
    private final int threadCount;
    private final long timeout;

    public EchoDatagramServerLoadTestRunner(
            final EchoDatagramServerInterface echDatagramServerInterface,
            final SocksServerInterface scksServerInterface,
            final int numberOfThreads,
            final long delayBetweenThreadsStart,
            final EchoDatagramServerTestRunnerFactory echDatagramServerTestRunnerFactory,
            final long tmt) {
        this.echoDatagramServerInterface = Objects.requireNonNull(
                echDatagramServerInterface);
        this.socksServerInterface = scksServerInterface;
        this.delayBetweenThreadsStarting = delayBetweenThreadsStart;
        this.echoDatagramServerTestRunnerFactory = Objects.requireNonNull(
                echDatagramServerTestRunnerFactory);
        this.threadCount = numberOfThreads;
        this.timeout = tmt;
    }

    public LoadTestRunnerResults run() throws IOException {
        LoadTestRunnerResults loadTestRunnerResults = new LoadTestRunnerResults(
                this.threadCount, this.delayBetweenThreadsStarting);
        ExecutorService executor =
                ExecutorsHelper.newVirtualThreadPerTaskExecutorOrElse(
                        ExecutorsHelper.newCachedThreadPoolBuilder());
        try {
            String socksServerHostAddress = null;
            int socksServerPort = -1;
            if (this.socksServerInterface != null) {
                this.socksServerInterface.start();
                socksServerHostAddress = this.socksServerInterface.getHostAddress();
                socksServerPort = this.socksServerInterface.getPort();
            }
            this.echoDatagramServerInterface.start();
            for (int i = 0; i < this.threadCount; i++) {
                executor.execute(new LoadTestRunnerWorker(
                        i * this.delayBetweenThreadsStarting,
                        this.echoDatagramServerTestRunnerFactory.newEchoDatagramServerTestRunner(
                                this.echoDatagramServerInterface.getInetAddress(),
                                this.echoDatagramServerInterface.getPort(),
                                socksServerHostAddress,
                                socksServerPort),
                        loadTestRunnerResults));
            }
            long startWaitTime = System.currentTimeMillis();
            do {
                try {
                    Thread.sleep(HALF_SECOND);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } while (loadTestRunnerResults.getCompletedThreadCount() < this.threadCount
                    && System.currentTimeMillis() - startWaitTime < this.timeout);
        } finally {
            executor.shutdownNow();
            if (!this.echoDatagramServerInterface.getState().equals(
                    EchoDatagramServerInterface.State.STOPPED)) {
                this.echoDatagramServerInterface.stop();
            }
            if (this.socksServerInterface != null
                    && !this.socksServerInterface.getState().equals(
                            SocksServerInterface.State.STOPPED)) {
                this.socksServerInterface.stop();
            }
        }
        return loadTestRunnerResults;
    }
    
}
