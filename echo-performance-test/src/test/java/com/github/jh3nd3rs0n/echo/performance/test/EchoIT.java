package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class EchoIT {

    private static final int INCREMENT_THREAD_COUNT = 10;
    private static final int INIT_THREAD_COUNT = 10;
    private static final int MAX_THREAD_COUNT = 200;
    private static final long TIMEOUT = 1;
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    private static Path performanceReport = null;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        performanceReport = PerformanceReportHelper.createPerformanceReport(
                className + ".txt", "Class " + className);
    }

    @Test
    public void testDatagramEchoServer() throws IOException {
        LoadTestRunnerResults results = new DatagramEchoServerLoadTestRunner()
                .setTimeout(TIMEOUT)
                .setTimeUnit(TIME_UNIT)
                .run(
                        new DatagramEchoServerTestFactoryImpl(),
                        INIT_THREAD_COUNT,
                        MAX_THREAD_COUNT,
                        INCREMENT_THREAD_COUNT);
        ThreadsRunnerResults lastSuccessfulThreadsRunnerResults =
                results.getLastSuccessfulThreadsRunnerResults();
        ThreadsRunnerResults unsuccessfulThreadsRunnerResults =
                results.getUnsuccessfulThreadsRunnerResults();
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        PerformanceReportHelper.writeToPerformanceReport(
                performanceReport,
                "Method " + methodName,
                lastSuccessfulThreadsRunnerResults,
                unsuccessfulThreadsRunnerResults);
        Assert.assertNotNull(lastSuccessfulThreadsRunnerResults);
    }

    @Test
    public void testEchoServer() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner()
                .setTimeout(TIMEOUT)
                .setTimeUnit(TIME_UNIT)
                .run(
                        new EchoServerTestFactoryImpl(),
                        INIT_THREAD_COUNT,
                        MAX_THREAD_COUNT,
                        INCREMENT_THREAD_COUNT);
        ThreadsRunnerResults lastSuccessfulThreadsRunnerResults =
                results.getLastSuccessfulThreadsRunnerResults();
        ThreadsRunnerResults unsuccessfulThreadsRunnerResults =
                results.getUnsuccessfulThreadsRunnerResults();
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        PerformanceReportHelper.writeToPerformanceReport(
                performanceReport,
                "Method " + methodName,
                lastSuccessfulThreadsRunnerResults,
                unsuccessfulThreadsRunnerResults);
        Assert.assertNotNull(lastSuccessfulThreadsRunnerResults);
    }

    private static final class DatagramEchoServerTestFactoryImpl extends DatagramEchoServerTestFactory {

        @Override
        public DatagramEchoServerTest newDatagramEchoServerTest(
                InetAddress datagramEchServerInetAddress,
                int datagramEchServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new DatagramEchoServerTestImpl(
                    datagramEchServerInetAddress,
                    datagramEchServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class DatagramEchoServerTestImpl extends DatagramEchoServerTest {

        public DatagramEchoServerTestImpl(
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
                        TestStringConstants.STRING_03,
                        this.datagramEchoServerInetAddress,
                        this.datagramEchoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

    private static final class EchoServerTestFactoryImpl extends EchoServerTestFactory {

        @Override
        public EchoServerTest newEchoServerTest(
                InetAddress echServerInetAddress,
                int echServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new EchoServerTestImpl(
                    echServerInetAddress,
                    echServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class EchoServerTestImpl extends EchoServerTest {

        public EchoServerTestImpl(
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
                        TestStringConstants.STRING_03,
                        this.echoServerInetAddress,
                        this.echoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
