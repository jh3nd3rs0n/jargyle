package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.load;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoServerBehindJargyleSocksServerUsingSslIT {

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

    private static SocksClient newSocks5ClientUsingSsl(
            final String socksServerHostAddress, final int socksServerPort) {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                        Boolean.TRUE),
                SslPropertySpecConstants.SSL_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        socksServerHostAddress,
                        socksServerPort)
                .newSocksClient(properties);
    }

    private static AbstractSocksServer newJargyleSocksServerUsingSsl() {
        return new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(Port.valueOf(0)),
                GeneralSettingSpecConstants.BACKLOG.newSetting(
                        NonNegativeInteger.valueOf(Server.BACKLOG)),
                DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_BYTES.newSetting(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                SslSettingSpecConstants.SSL_KEY_STORE_BYTES.newSetting(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
    }

    @Test
    public void testEchoServerBehindJargyleSocksServerUsingSsl() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
                EchoServer.newInstance(0),
                newJargyleSocksServerUsingSsl(),
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new EchoServerTestRunnerFactoryImpl(),
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
                    new SocksSocketFactory(
                            newSocks5ClientUsingSsl(
                                    this.socksServerInetAddress.getHostAddress(),
                                    this.socksServerPort)));
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
