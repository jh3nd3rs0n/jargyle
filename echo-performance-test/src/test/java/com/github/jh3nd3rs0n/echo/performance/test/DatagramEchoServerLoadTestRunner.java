package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class DatagramEchoServerLoadTestRunner {

    private static final long DEFAULT_TIMEOUT = 1;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    private ConfigurationFactory configurationFactory;
    private long timeout;
    private TimeUnit timeUnit;

    public DatagramEchoServerLoadTestRunner() {
        this.configurationFactory = null;
        this.timeout = DEFAULT_TIMEOUT;
        this.timeUnit = DEFAULT_TIME_UNIT;
    }

    public DatagramEchoServerLoadTestRunner setConfigurationFactory(
            final ConfigurationFactory factory) {
        this.configurationFactory = factory;
        return this;
    }

    public DatagramEchoServerLoadTestRunner setTimeout(final long tmt) {
        this.timeout = tmt;
        return this;
    }

    public DatagramEchoServerLoadTestRunner setTimeUnit(final TimeUnit unit) {
        this.timeUnit = unit;
        return this;
    }

    public LoadTestRunnerResults run(
            final DatagramEchoServerTestFactory datagramEchoServerTestFactory,
            final int initialThreadCount,
            final int maxThreadCount,
            final int incrementThreadCount) throws IOException {
        ThreadsRunnerResults lastSuccessfulThreadsRunnerResults = null;
        ThreadsRunnerResults unsuccessfulThreadsRunnerResults = null;
        for (int i = initialThreadCount; i <= maxThreadCount; i += incrementThreadCount) {
            SocksServer socksServer = (this.configurationFactory == null) ?
                    null : new SocksServer(this.configurationFactory.newConfiguration());
            DatagramEchoServer datagramEchoServer = DatagramEchoServerHelper.newDatagramEchoServer();
            try {
                String socksServerHostAddress = null;
                int socksServerPort = -1;
                if (socksServer != null) {
                    socksServer.start();
                    socksServerHostAddress = socksServer.getHost().toString();
                    socksServerPort = socksServer.getPort().intValue();
                }
                datagramEchoServer.start();
                ThreadsRunnerResults threadsRunnerResults = new ThreadsRunner()
                        .setTimeout(this.timeout)
                        .setTimeUnit(this.timeUnit)
                        .run(datagramEchoServerTestFactory.newDatagramEchoServerTest(
                                datagramEchoServer.getInetAddress(),
                                datagramEchoServer.getPort(),
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
                if (!datagramEchoServer.getState().equals(
                        DatagramEchoServer.State.STOPPED)) {
                    datagramEchoServer.stop();
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
