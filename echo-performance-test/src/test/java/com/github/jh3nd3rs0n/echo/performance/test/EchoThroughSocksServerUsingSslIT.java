package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.constants.TestResourceConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoThroughSocksServerUsingSslIT {

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

    private static Configuration newConfigurationUsingSsl() {
        return Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(Port.valueOf(0)),
                GeneralSettingSpecConstants.BACKLOG.newSetting(
                        NonNegativeInteger.valueOf(EchoServerHelper.BACKLOG)),
                DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
                SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString())));
    }

    private static SocksClient newSocks5ClientUsingSsl(
            final String socksServerHostAddress, final int socksServerPort) {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                        Boolean.TRUE),
                SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        TestResourceConstants.ECHO_PERFORMANCE_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
        return Scheme.SOCKS5.newSocksServerUri(
                        socksServerHostAddress,
                        socksServerPort)
                .newSocksClient(properties);
    }

    @Test
    public void testDatagramEchoServerThroughSocksServerUsingSsl() throws IOException {
        LoadTestRunnerResults results = new DatagramEchoServerLoadTestRunner(
                newConfigurationUsingSsl(),
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
    public void testEchoServerThroughSocksServerUsingSsl() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
                newConfigurationUsingSsl(),
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
            DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                    newSocks5ClientUsingSsl(
                            this.socksServerHostAddress,
                            this.socksServerPort)
                            .newSocksNetObjectFactory());
            try {
                datagramEchoClient.echo(
                        TestStringConstants.STRING_03,
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
            EchoClient echoClient = new EchoClient(
                    newSocks5ClientUsingSsl(
                            this.socksServerHostAddress,
                            this.socksServerPort)
                            .newSocksNetObjectFactory());
            try {
                echoClient.echo(
                        TestStringConstants.STRING_03,
                        this.echoServerInetAddress,
                        this.echoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
