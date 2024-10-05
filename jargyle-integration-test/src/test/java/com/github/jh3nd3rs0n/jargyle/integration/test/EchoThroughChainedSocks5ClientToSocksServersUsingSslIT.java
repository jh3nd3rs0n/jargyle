package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.*;
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

public class EchoThroughChainedSocks5ClientToSocksServersUsingSslIT {

    private static DatagramEchoServer datagramEchoServer;
    private static int datagramEchoServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static List<SocksServer> socksServersUsingSsl;
    private static int socksServerPort1UsingSsl;
    private static int socksServerPort2UsingSsl;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
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
                        Port.valueOf(0)))));
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
                        TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()))));
        socksServer2.start();
        socksServerPort2UsingSsl = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        return socksServerList;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = new DatagramEchoServer(0);
        datagramEchoServer.start();
        datagramEchoServerPort = datagramEchoServer.getPort();
        echoServer = new EchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServersUsingSsl = newSocksServersUsingSsl();
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
        if (socksServersUsingSsl != null) {
            stopSocksServers(socksServersUsingSsl);
        }
        ThreadHelper.interruptableSleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
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

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
