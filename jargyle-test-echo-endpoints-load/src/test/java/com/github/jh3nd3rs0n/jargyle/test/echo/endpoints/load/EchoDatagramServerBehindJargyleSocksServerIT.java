package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.load;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.*;
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


public class EchoDatagramServerBehindJargyleSocksServerIT {

    private static final long DELAY_BETWEEN_THREADS_STARTING = 400;
    private static final int THREAD_COUNT = 100;
    private static final long TIMEOUT = 60000 * 5;

    private static Path loadTestReport = null;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        loadTestReport = LoadTestReportHelper.createLoadTestReport(
                className + ".txt", "Class " + className);
    }

    private static SocksClient newSocks5Client(
            final String socksServerHostAddress, final int socksServerPort) {
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        socksServerHostAddress,
                        socksServerPort)
                .newSocksClient(Properties.of());
    }

    private static AbstractSocksServer newJargyleSocksServer() {
        return new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(Port.valueOf(0)),
                GeneralSettingSpecConstants.BACKLOG.newSetting(
                        NonNegativeInteger.valueOf(Server.BACKLOG)),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
    }

    @Test
    public void testEchoDatagramServerBehindJargyleSocksServer() throws IOException {
        LoadTestRunnerResults results = new EchoDatagramServerLoadTestRunner(
                EchoDatagramServer.newInstance(0),
                newJargyleSocksServer(),
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new EchoDatagramServerTestRunnerFactoryImpl(),
                TIMEOUT)
                .run();
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        LoadTestReportHelper.writeToLoadTestReport(
                loadTestReport,
                "Method " + methodName,
                results);
        Assert.assertNotNull(results);
    }

    private static final class EchoDatagramServerTestRunnerFactoryImpl extends EchoDatagramServerTestRunnerFactory {

        @Override
        public EchoDatagramServerTestRunner newEchoDatagramServerTestRunner(
                InetAddress echDatagramServerInetAddress,
                int echDatagramServerPort,
                InetAddress scksServerInetAddress,
                int scksServerPort) {
            return new EchoDatagramServerTestRunnerImpl(
                    echDatagramServerInetAddress,
                    echDatagramServerPort,
                    scksServerInetAddress,
                    scksServerPort);
        }

    }

    private static final class EchoDatagramServerTestRunnerImpl extends EchoDatagramServerTestRunner {

        public EchoDatagramServerTestRunnerImpl(
                InetAddress echDatagramServerInetAddress,
                int echDatagramServerPort,
                InetAddress scksServerInetAddress,
                int scksServerPort) {
            super(
                    echDatagramServerInetAddress,
                    echDatagramServerPort,
                    scksServerInetAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                    new SocksClientToDatagramSocketFactoryAdapter(
                            newSocks5Client(
                                    this.socksServerInetAddress.getHostAddress(),
                                    this.socksServerPort)));
            try {
                echoDatagramClient.echo(
                        StringConstants.STRING_05,
                        this.echoDatagramServerInetAddress,
                        this.echoDatagramServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
