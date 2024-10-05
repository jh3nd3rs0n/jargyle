package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.integration.test.DatagramEchoClient;
import com.github.jh3nd3rs0n.jargyle.integration.test.EchoClient;
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
    public void testDatagramEchoServer() throws IOException {
        LoadTestRunnerResults results = new DatagramEchoServerLoadTestRunner(
                null,
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new DatagramEchoServerTestRunnerFactoryImpl(),
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
    public void testEchoServer() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
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

    private static final class DatagramEchoServerTestRunnerFactoryImpl extends DatagramEchoServerTestRunnerFactory {

        @Override
        public DatagramEchoServerTestRunner newDatagramEchoServerTestRunner(
                InetAddress datagramEchServerInetAddress,
                int datagramEchServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new DatagramEchoServerTestRunnerImpl(
                    datagramEchServerInetAddress,
                    datagramEchServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class DatagramEchoServerTestRunnerImpl extends DatagramEchoServerTestRunner {

        public DatagramEchoServerTestRunnerImpl(
                InetAddress datagramEchServerInetAddress,
                int datagramEchServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            super(
                    datagramEchServerInetAddress,
                    datagramEchServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            try {
                new DatagramEchoClient().echo(
                        TestStringConstants.STRING_04,
                        this.datagramEchoServerInetAddress,
                        this.datagramEchoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

    private static final class EchoServerTestRunnerFactoryImpl extends EchoServerTestRunnerFactory {

        @Override
        public EchoServerTestRunner newEchoServerTestRunner(
                InetAddress echServerInetAddress,
                int echServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new EchoServerTestRunnerImpl(
                    echServerInetAddress,
                    echServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class EchoServerTestRunnerImpl extends EchoServerTestRunner {

        public EchoServerTestRunnerImpl(
                InetAddress echServerInetAddress,
                int echServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            super(
                    echServerInetAddress,
                    echServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            try {
                new EchoClient().echo(
                        TestStringConstants.STRING_04,
                        this.echoServerInetAddress,
                        this.echoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
