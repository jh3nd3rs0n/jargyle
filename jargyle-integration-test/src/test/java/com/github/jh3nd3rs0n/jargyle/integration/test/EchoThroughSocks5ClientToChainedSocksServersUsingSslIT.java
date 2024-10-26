package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
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

public class EchoThroughSocks5ClientToChainedSocksServersUsingSslIT {

    private static List<SocksServer> chainedSocksServersUsingSsl;
    private static int chainedSocksServerPort1UsingSsl;

    private static DatagramTestServer echoDatagramTestServer;
    private static int echoDatagramTestServerPort;
    private static TestServer echoTestServer;
    private static int echoTestServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<SocksServer> newChainedSocksServersUsingSsl() throws IOException {
        List<SocksServer> socksServerList = new ArrayList<>();
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
        int chainedSocksServerPort2UsingSsl = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        SocksServer socksServer1 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        Scheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort2UsingSsl)),
                ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED.newSetting(Boolean.TRUE),
                ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_INPUT_STREAM.newSetting(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED.newSetting(Boolean.TRUE),
                ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_INPUT_STREAM.newSetting(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        chainedSocksServerPort1UsingSsl = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        return socksServerList;
    }

    private static SocksClient newSocks5ClientToChainedSocksServersUsingSsl() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        chainedSocksServerPort1UsingSsl)
                .newSocksClient(Properties.of());
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoDatagramTestServer = EchoDatagramTestServerHelper.newEchoDatagramTestServer(0);
        echoDatagramTestServer.start();
        echoDatagramTestServerPort = echoDatagramTestServer.getPort();
        echoTestServer = EchoTestServerHelper.newEchoTestServer(0);
        echoTestServer.start();
        echoTestServerPort = echoTestServer.getPort();
        chainedSocksServersUsingSsl = newChainedSocksServersUsingSsl();
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
        if (chainedSocksServersUsingSsl != null) {
            stopSocksServers(chainedSocksServersUsingSsl);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSsl04() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSsl05() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSsl04() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSsl05() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSsl04() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSsl05() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_05;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

}
