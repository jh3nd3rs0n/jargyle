package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public final class EchoServerLoadTestRunner {

    private static final int HALF_SECOND = 500;

    private final EchoServerInterface echoServerInterface;
    private final SocksServerInterface socksServerInterface;
    private final long delayBetweenThreadsStarting;
    private final EchoServerTestRunnerFactory echoServerTestRunnerFactory;
    private final int threadCount;
    private final long timeout;

    public EchoServerLoadTestRunner(
            final EchoServerInterface echServerInterface,
            final SocksServerInterface scksServerInterface,
            final int numberOfThreads,
            final long delayBetweenThreadsStart,
            final EchoServerTestRunnerFactory echServerTestRunnerFactory,
            final long tmt) {
        this.echoServerInterface = Objects.requireNonNull(
                echServerInterface);
        this.socksServerInterface = scksServerInterface;
        this.delayBetweenThreadsStarting = delayBetweenThreadsStart;
        this.echoServerTestRunnerFactory = Objects.requireNonNull(
                echServerTestRunnerFactory);
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
            this.echoServerInterface.start();
            for (int i = 0; i < this.threadCount; i++) {
                executor.execute(new LoadTestRunnerWorker(
                        i * this.delayBetweenThreadsStarting,
                        this.echoServerTestRunnerFactory.newEchoServerTestRunner(
                                this.echoServerInterface.getInetAddress(),
                                this.echoServerInterface.getPort(),
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
            if (!this.echoServerInterface.getState().equals(
                    EchoServerInterface.State.STOPPED)) {
                this.echoServerInterface.stop();
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
