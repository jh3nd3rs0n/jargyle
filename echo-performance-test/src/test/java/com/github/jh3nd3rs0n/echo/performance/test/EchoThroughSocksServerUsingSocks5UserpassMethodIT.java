package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoThroughSocksServerUsingSocks5UserpassMethodIT {

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

    private static Configuration newConfigurationUsingSocks5UserpassMethod() {
        return Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(Port.valueOf(0)),
                GeneralSettingSpecConstants.BACKLOG.newSetting(
                        NonNegativeInteger.valueOf(EchoServerHelper.BACKLOG)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Aladdin:opensesame"))));
    }

    private static SocksClient newSocks5ClientUsingSocks5UserpassMethod(
            final String socksServerHostAddress,
            final int socksServerPort) {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                        "Aladdin"),
                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                        EncryptedPassword.newInstance("opensesame".toCharArray())));
        return Scheme.SOCKS5.newSocksServerUri(
                        socksServerHostAddress,
                        socksServerPort)
                .newSocksClient(properties);
    }

    @Test
    public void testDatagramEchoServerThroughSocksServerUsingSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new DatagramEchoServerLoadTestRunner(
                newConfigurationUsingSocks5UserpassMethod(),
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
    public void testEchoServerThroughSocksServerUsingSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
                newConfigurationUsingSocks5UserpassMethod(),
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
                    newSocks5ClientUsingSocks5UserpassMethod(
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
                    newSocks5ClientUsingSocks5UserpassMethod(
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
