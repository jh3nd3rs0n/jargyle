package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.TestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.security.TestKeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.jh3nd3rs0n.jargyle.integration.test.SocksServersHelper.stopSocksServers;
import static org.junit.Assert.assertEquals;

public class EchoThroughChainedSocks5ClientToSocksServersUsingSslIT {

    private static DatagramTestServer echoDatagramTestServer;
    private static int echoDatagramTestServerPort;
    private static TestServer echoTestServer;
    private static int echoTestServerPort;

    private static List<SocksServer> socksServersUsingSsl;
    private static int socksServerPort1UsingSsl;
    private static int socksServerPort2UsingSsl;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static SocksClient newChainedSocks5ClientToSocksServersUsingSsl() {
        SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort1UsingSsl)
                .newSocksClient(Properties.of());
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort2UsingSsl)
                .newSocksClient(Properties.of(
                                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                                        Boolean.TRUE),
                                DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
                                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                                        Boolean.TRUE),
                                SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
                                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString())),
                        client1);
    }

    private static List<SocksServer> newSocksServersUsingSsl() throws IOException {
        List<SocksServer> socksServerList = new ArrayList<>();
        SocksServer socksServer1 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        socksServerPort1UsingSsl = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        SocksServer socksServer2 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_INPUT_STREAM.newSetting(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                SslSettingSpecConstants.SSL_KEY_STORE_INPUT_STREAM.newSetting(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        socksServerPort2UsingSsl = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        return socksServerList;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoDatagramTestServer = EchoDatagramTestServerHelper.newEchoDatagramTestServer(0);
        echoDatagramTestServer.start();
        echoDatagramTestServerPort = echoDatagramTestServer.getPort();
        echoTestServer = EchoTestServerHelper.newEchoTestServer(0);
        echoTestServer.start();
        echoTestServerPort = echoTestServer.getPort();
        socksServersUsingSsl = newSocksServersUsingSsl();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (echoDatagramTestServer != null
                && !echoDatagramTestServer.getState().equals(DatagramTestServer.State.STOPPED)) {
            echoDatagramTestServer.stop();
        }
        if (echoTestServer != null
                && !echoTestServer.getState().equals(TestServer.State.STOPPED)) {
            echoTestServer.stop();
        }
        if (socksServersUsingSsl != null) {
            stopSocksServers(socksServersUsingSsl);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServersUsingSsl05() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServersUsingSsl05() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServersUsingSsl05() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_05;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

}
