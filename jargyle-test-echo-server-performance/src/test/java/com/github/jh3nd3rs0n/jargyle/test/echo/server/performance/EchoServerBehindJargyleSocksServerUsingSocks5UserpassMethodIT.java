package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
import com.github.jh3nd3rs0n.jargyle.test.echo.AbstractSocksServer;
import com.github.jh3nd3rs0n.jargyle.test.echo.EchoClient;
import com.github.jh3nd3rs0n.jargyle.test.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.test.echo.JargyleSocksServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoServerBehindJargyleSocksServerUsingSocks5UserpassMethodIT {

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

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
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
                .newSocksClient(properties)
                .newSocksNetObjectFactory();
    }

    private static AbstractSocksServer newJargyleSocksServerUsingSocks5UserpassMethod() {
        return new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(Port.valueOf(0)),
                GeneralSettingSpecConstants.BACKLOG.newSetting(
                        NonNegativeInteger.valueOf(Server.BACKLOG)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Aladdin:opensesame")),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
    }

    @Test
    public void testEchoServerBehindJargyleSocksServerUsingSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
                EchoServer.newInstance(0),
                newJargyleSocksServerUsingSocks5UserpassMethod(),
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
            EchoClient echoClient = new EchoClient(
                    newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
                            this.socksServerInetAddress.getHostAddress(),
                            this.socksServerPort));
            try {
                echoClient.echo(
                        StringConstants.STRING_05,
                        this.echoServerInetAddress,
                        this.echoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
