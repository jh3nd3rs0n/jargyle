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
import java.util.concurrent.TimeUnit;

public class EchoThroughSocksServerUsingSocks5UserpassMethodIT {

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
        LoadTestRunnerResults results = new DatagramEchoServerLoadTestRunner()
                .setConfigurationFactory(new ConfigurationFactoryImpl())
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
    public void testEchoServerThroughSocksServerUsingSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner()
                .setConfigurationFactory(new ConfigurationFactoryImpl())
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

    private static final class ConfigurationFactoryImpl extends ConfigurationFactory {

        @Override
        public Configuration newConfiguration() {
            return newConfigurationUsingSocks5UserpassMethod();
        }

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
