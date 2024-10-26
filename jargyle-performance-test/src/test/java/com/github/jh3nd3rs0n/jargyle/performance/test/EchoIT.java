package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.integration.test.EchoDatagramTestClient;
import com.github.jh3nd3rs0n.jargyle.integration.test.EchoTestClient;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoIT {

    private static final long DELAY_BETWEEN_THREADS_STARTING = 500;
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
    public void testDatagramTestServer() throws IOException {
        LoadTestRunnerResults results = new EchoDatagramTestServerLoadTestRunner(
                null,
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new EchoDatagramTestServerTestRunnerFactoryImpl(),
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

    @Test
    public void testTestServer() throws IOException {
        LoadTestRunnerResults results = new EchoTestServerLoadTestRunner(
                null,
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new EchoTestServerTestRunnerFactoryImpl(),
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

    private static final class EchoDatagramTestServerTestRunnerFactoryImpl extends EchoDatagramTestServerTestRunnerFactory {

        @Override
        public EchoDatagramTestServerTestRunner newEchoDatagramTestServerTestRunner(
                InetAddress echDatagramTestServerInetAddress,
                int echDatagramTestServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new EchoDatagramTestServerTestRunnerImpl(
                    echDatagramTestServerInetAddress,
                    echDatagramTestServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class EchoDatagramTestServerTestRunnerImpl extends EchoDatagramTestServerTestRunner {

        public EchoDatagramTestServerTestRunnerImpl(
                InetAddress echDatagramTestServerInetAddress,
                int echDatagramTestServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            super(
                    echDatagramTestServerInetAddress,
                    echDatagramTestServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            try {
                new EchoDatagramTestClient().echo(
                        TestStringConstants.STRING_05,
                        this.echoDatagramTestServerInetAddress,
                        this.echoDatagramTestServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

    private static final class EchoTestServerTestRunnerFactoryImpl extends EchoTestServerTestRunnerFactory {

        @Override
        public EchoTestServerTestRunner newEchoTestServerTestRunner(
                InetAddress echTestServerInetAddress,
                int echTestServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new EchoTestServerTestRunnerImpl(
                    echTestServerInetAddress,
                    echTestServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class EchoTestServerTestRunnerImpl extends EchoTestServerTestRunner {

        public EchoTestServerTestRunnerImpl(
                InetAddress echTestServerInetAddress,
                int echTestServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            super(
                    echTestServerInetAddress,
                    echTestServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            try {
                new EchoTestClient().echo(
                        TestStringConstants.STRING_05,
                        this.echoTestServerInetAddress,
                        this.echoTestServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
