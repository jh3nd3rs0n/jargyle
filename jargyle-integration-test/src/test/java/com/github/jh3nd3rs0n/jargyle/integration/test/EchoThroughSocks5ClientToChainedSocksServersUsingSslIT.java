package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.*;
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

    private static DatagramEchoServer datagramEchoServer;
    private static int datagramEchoServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
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
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()))));
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
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()))));
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
        datagramEchoServer = new DatagramEchoServer(0);
        datagramEchoServer.start();
        datagramEchoServerPort = datagramEchoServer.getPort();
        echoServer = new EchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        chainedSocksServersUsingSsl = newChainedSocksServersUsingSsl();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (datagramEchoServer != null
                && !datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
            datagramEchoServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        if (chainedSocksServersUsingSsl != null) {
            stopSocksServers(chainedSocksServersUsingSsl);
        }
        ThreadHelper.interruptableSleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl04() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSsl04() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
