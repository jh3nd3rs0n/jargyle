package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;
import com.github.jh3nd3rs0n.jargyle.test.echo.AbstractSocksServer;
import com.github.jh3nd3rs0n.jargyle.test.echo.EchoDatagramServer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public final class EchoDatagramServerLoadTestRunner {

    private static final int HALF_SECOND = 500;

    private final EchoDatagramServer echoDatagramServer;
    private final AbstractSocksServer socksServer;
    private final long delayBetweenThreadsStarting;
    private final EchoDatagramServerTestRunnerFactory echoDatagramServerTestRunnerFactory;
    private final int threadCount;
    private final long timeout;

    public EchoDatagramServerLoadTestRunner(
            final EchoDatagramServer echDatagramServer,
            final AbstractSocksServer scksServer,
            final int numberOfThreads,
            final long delayBetweenThreadsStart,
            final EchoDatagramServerTestRunnerFactory echDatagramServerTestRunnerFactory,
            final long tmt) {
        this.echoDatagramServer = Objects.requireNonNull(echDatagramServer);
        this.socksServer = scksServer;
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
            InetAddress socksServerInetAddress = null;
            int socksServerPort = -1;
            if (this.socksServer != null) {
                this.socksServer.start();
                socksServerInetAddress = this.socksServer.getInetAddress();
                socksServerPort = this.socksServer.getPort();
            }
            this.echoDatagramServer.start();
            for (int i = 0; i < this.threadCount; i++) {
                executor.execute(new LoadTestRunnerWorker(
                        i * this.delayBetweenThreadsStarting,
                        this.echoDatagramServerTestRunnerFactory.newEchoDatagramServerTestRunner(
                                this.echoDatagramServer.getInetAddress(),
                                this.echoDatagramServer.getPort(),
                                socksServerInetAddress,
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
            if (!this.echoDatagramServer.getState().equals(
                    EchoDatagramServer.State.STOPPED)) {
                this.echoDatagramServer.stop();
            }
            if (this.socksServer != null
                    && !this.socksServer.getState().equals(
                            AbstractSocksServer.State.STOPPED)) {
                this.socksServer.stop();
            }
        }
        return loadTestRunnerResults;
    }
    
}
