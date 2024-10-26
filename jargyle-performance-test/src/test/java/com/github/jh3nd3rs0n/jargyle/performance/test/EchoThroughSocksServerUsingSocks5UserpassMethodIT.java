package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.integration.test.EchoDatagramTestClient;
import com.github.jh3nd3rs0n.jargyle.integration.test.EchoTestClient;
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
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.TestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
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
                        NonNegativeInteger.valueOf(TestServer.BACKLOG)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Aladdin:opensesame")),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE))));
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
    public void testDatagramTestServerThroughSocksServerUsingSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new EchoDatagramTestServerLoadTestRunner(
                newConfigurationUsingSocks5UserpassMethod(),
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
    public void testTestServerThroughSocksServerUsingSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new EchoTestServerLoadTestRunner(
                newConfigurationUsingSocks5UserpassMethod(),
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
            EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                    newSocks5ClientUsingSocks5UserpassMethod(
                            this.socksServerHostAddress,
                            this.socksServerPort)
                            .newSocksNetObjectFactory());
            try {
                echoDatagramTestClient.echo(
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
            EchoTestClient echoTestClient = new EchoTestClient(
                    newSocks5ClientUsingSocks5UserpassMethod(
                            this.socksServerHostAddress,
                            this.socksServerPort)
                            .newSocksNetObjectFactory());
            try {
                echoTestClient.echo(
                        TestStringConstants.STRING_05,
                        this.echoTestServerInetAddress,
                        this.echoTestServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
