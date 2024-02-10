package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class EchoServerLoadTestRunner {

    private static final long DEFAULT_TIMEOUT = 1;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    private ConfigurationFactory configurationFactory;
    private long timeout;
    private TimeUnit timeUnit;

    public EchoServerLoadTestRunner() {
        this.configurationFactory = null;
        this.timeout = DEFAULT_TIMEOUT;
        this.timeUnit = DEFAULT_TIME_UNIT;
    }

    public EchoServerLoadTestRunner setConfigurationFactory(
            final ConfigurationFactory factory) {
        this.configurationFactory = factory;
        return this;
    }

    public EchoServerLoadTestRunner setTimeout(final long tmt) {
        this.timeout = tmt;
        return this;
    }

    public EchoServerLoadTestRunner setTimeUnit(final TimeUnit unit) {
        this.timeUnit = unit;
        return this;
    }

    public LoadTestRunnerResults run(
            final EchoServerTestFactory echoServerTestFactory,
            final int initialThreadCount,
            final int maxThreadCount,
            final int incrementThreadCount) throws IOException {
        ThreadsRunnerResults lastSuccessfulThreadsRunnerResults = null;
        ThreadsRunnerResults unsuccessfulThreadsRunnerResults = null;
        for (int i = initialThreadCount; i <= maxThreadCount; i += incrementThreadCount) {
            SocksServer socksServer = (this.configurationFactory == null) ?
                    null : new SocksServer(this.configurationFactory.newConfiguration());
            EchoServer echoServer = EchoServerHelper.newEchoServer();
            try {
                String socksServerHostAddress = null;
                int socksServerPort = -1;
                if (socksServer != null) {
                    socksServer.start();
                    socksServerHostAddress = socksServer.getHost().toString();
                    socksServerPort = socksServer.getPort().intValue();
                }
                echoServer.start();
                ThreadsRunnerResults threadsRunnerResults = new ThreadsRunner()
                        .setTimeout(this.timeout)
                        .setTimeUnit(this.timeUnit)
                        .run(echoServerTestFactory.newEchoServerTest(
                                echoServer.getInetAddress(),
                                echoServer.getPort(),
                                socksServerHostAddress,
                                socksServerPort),
                                i);
                if (threadsRunnerResults.getActualSuccessfulThreadCount() == i) {
                    lastSuccessfulThreadsRunnerResults = threadsRunnerResults;
                } else {
                    unsuccessfulThreadsRunnerResults = threadsRunnerResults;
                    break;
                }
            } finally {
                if (!echoServer.getState().equals(
                        EchoServer.State.STOPPED)) {
                    echoServer.stop();
                }
                if (socksServer != null && !socksServer.getState().equals(
                        SocksServer.State.STOPPED)) {
                    socksServer.stop();
                }
            }
        }
        return new LoadTestRunnerResults(
                lastSuccessfulThreadsRunnerResults, unsuccessfulThreadsRunnerResults);
    }

}
