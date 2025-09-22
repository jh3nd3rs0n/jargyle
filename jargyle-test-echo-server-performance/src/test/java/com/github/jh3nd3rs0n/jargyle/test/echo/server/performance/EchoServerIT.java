package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import com.github.jh3nd3rs0n.jargyle.test.echo.EchoClient;
import com.github.jh3nd3rs0n.jargyle.test.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoServerIT {

    private static final long DELAY_BETWEEN_THREADS_STARTING = 400;
    private static final int THREAD_COUNT = 100;
    private static final long TIMEOUT = 60000 * 5;

    private static Path performanceReport = null;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        performanceReport = PerformanceReportHelper.createPerformanceReport(
                className + ".txt", "Class " + className);
    }

    @Test
    public void testEchoServer() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
                EchoServer.newInstance(0),
                null,
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new EchoServerTestRunnerFactoryImpl(),
                TIMEOUT)
                .run();
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        PerformanceReportHelper.writeToPerformanceReport(
                performanceReport,
                "Method " + methodName,
                results);
        Assert.assertNotNull(results);
    }

    private static final class EchoServerTestRunnerFactoryImpl extends EchoServerTestRunnerFactory {

        @Override
        public EchoServerTestRunner newEchoServerTestRunner(
                InetAddress echServerInetAddress,
                int echServerPort,
                InetAddress scksServerInetAddress,
                int scksServerPort) {
            return new EchoServerTestRunnerImpl(
                    echServerInetAddress,
                    echServerPort,
                    scksServerInetAddress,
                    scksServerPort);
        }

    }

    private static final class EchoServerTestRunnerImpl extends EchoServerTestRunner {

        public EchoServerTestRunnerImpl(
                InetAddress echServerInetAddress,
                int echServerPort,
                InetAddress scksServerInetAddress,
                int scksServerPort) {
            super(
                    echServerInetAddress,
                    echServerPort,
                    scksServerInetAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            try {
                new EchoClient().echo(
                        StringConstants.STRING_05,
                        this.echoServerInetAddress,
                        this.echoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
