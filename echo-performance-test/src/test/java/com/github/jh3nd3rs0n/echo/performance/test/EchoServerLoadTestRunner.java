package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public final class EchoServerLoadTestRunner {

    private static final int HALF_SECOND = 500;

    private final Configuration configuration;
    private final long delayBetweenThreadsStarting;
    private final EchoServerTestRunnerFactory echoServerTestRunnerFactory;
    private final int threadCount;
    private final long timeout;

    public EchoServerLoadTestRunner(
            final Configuration config,
            final int numberOfThreads,
            final long delayBetweenThreadsStart,
            final EchoServerTestRunnerFactory echServerTestRunnerFactory,
            final long tmt) {
        this.configuration = config;
        this.delayBetweenThreadsStarting = delayBetweenThreadsStart;
        this.echoServerTestRunnerFactory = echServerTestRunnerFactory;
        this.threadCount = numberOfThreads;
        this.timeout = tmt;
    }

    public LoadTestRunnerResults run() throws IOException {
        SocksServer socksServer = (this.configuration == null) ?
                null : new SocksServer(this.configuration);
        EchoServer echoServer = EchoServerHelper.newEchoServer();
        LoadTestRunnerResults loadTestRunnerResults = new LoadTestRunnerResults(
                this.threadCount, this.delayBetweenThreadsStarting);
        ExecutorService executor = ExecutorHelper.newExecutor();
        try {
            String socksServerHostAddress = null;
            int socksServerPort = -1;
            if (socksServer != null) {
                socksServer.start();
                socksServerHostAddress = socksServer.getHost().toString();
                socksServerPort = socksServer.getPort().intValue();
            }
            echoServer.start();
            for (int i = 0; i < this.threadCount; i++) {
                executor.execute(new LoadTestRunnerWorker(
                        i * this.delayBetweenThreadsStarting,
                        this.echoServerTestRunnerFactory.newEchoServerTestRunner(
                                echoServer.getInetAddress(),
                                echoServer.getPort(),
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
            if (!echoServer.getState().equals(
                    EchoServer.State.STOPPED)) {
                echoServer.stop();
            }
            if (socksServer != null && !socksServer.getState().equals(
                    SocksServer.State.STOPPED)) {
                socksServer.stop();
            }
        }
        return loadTestRunnerResults;
    }
    
}
