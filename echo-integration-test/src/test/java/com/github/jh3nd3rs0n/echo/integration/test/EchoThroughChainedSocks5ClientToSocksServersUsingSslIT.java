package com.github.jh3nd3rs0n.echo.integration.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;
import com.github.jh3nd3rs0n.test.help.constants.TestResourceConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.jh3nd3rs0n.echo.integration.test.SocksServersHelper.newStartedSocksServers;
import static com.github.jh3nd3rs0n.echo.integration.test.SocksServersHelper.stopSocksServers;
import static org.junit.Assert.assertEquals;

public class EchoThroughChainedSocks5ClientToSocksServersUsingSslIT {

    private static final int SOCKS_SERVER_PORT_1_USING_SSL = 7100;
    private static final int SOCKS_SERVER_PORT_2_USING_SSL = 7200;

    private static DatagramEchoServer datagramEchoServer;
    private static EchoServer echoServer;

    private static List<SocksServer> socksServersUsingSsl;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.MINUTES)
            .withLookingForStuckThread(true)
            .build();

    private static SocksClient newChainedSocks5ClientToSocksServersUsingSsl() {
        SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        Integer.valueOf(SOCKS_SERVER_PORT_1_USING_SSL))
                .newSocksClient(Properties.of());
        SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        Integer.valueOf(SOCKS_SERVER_PORT_2_USING_SSL))
                .newSocksClient(Properties.of(
                                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                                        Boolean.TRUE),
                                DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
                                        TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                                        TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
                                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                                        Boolean.TRUE),
                                SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
                                        TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                                        TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString())),
                        client1);
        return client2;
    }

    private static List<Configuration> newConfigurationsUsingSsl() {
        return Arrays.asList(
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(SOCKS_SERVER_PORT_1_USING_SSL)))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(SOCKS_SERVER_PORT_2_USING_SSL)),
                        DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                        DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                        DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
                        SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                        SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                        SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()))));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = new DatagramEchoServer();
        echoServer = new EchoServer();
        datagramEchoServer.start();
        echoServer.start();
        socksServersUsingSsl = newStartedSocksServers(
                newConfigurationsUsingSsl());
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
            datagramEchoServer.stop();
        }
        if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        stopSocksServers(socksServersUsingSsl);
        ThreadHelper.sleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
