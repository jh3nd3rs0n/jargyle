package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.load;

import com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.AbstractSocksServer;
import com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.EchoServer;
import com.github.jh3nd3rs0n.jargyle.test.help.concurrent.ExecutorsHelper;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public final class EchoServerLoadTestRunner {

    private static final int HALF_SECOND = 500;

    private final EchoServer echoServer;
    private final AbstractSocksServer socksServer;
    private final long delayBetweenThreadsStarting;
    private final EchoServerTestRunnerFactory echoServerTestRunnerFactory;
    private final int threadCount;
    private final long timeout;

    public EchoServerLoadTestRunner(
            final EchoServer echServer,
            final AbstractSocksServer scksServer,
            final int numberOfThreads,
            final long delayBetweenThreadsStart,
            final EchoServerTestRunnerFactory echServerTestRunnerFactory,
            final long tmt) {
        this.echoServer = Objects.requireNonNull(echServer);
        this.socksServer = scksServer;
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
            InetAddress socksServerInetAddress = null;
            int socksServerPort = -1;
            if (this.socksServer != null) {
                this.socksServer.start();
                socksServerInetAddress = this.socksServer.getInetAddress();
                socksServerPort = this.socksServer.getPort();
            }
            this.echoServer.start();
            for (int i = 0; i < this.threadCount; i++) {
                executor.execute(new LoadTestRunnerWorker(
                        i * this.delayBetweenThreadsStarting,
                        this.echoServerTestRunnerFactory.newEchoServerTestRunner(
                                this.echoServer.getInetAddress(),
                                this.echoServer.getPort(),
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
            if (!this.echoServer.getState().equals(
                    EchoServer.State.STOPPED)) {
                this.echoServer.stop();
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
