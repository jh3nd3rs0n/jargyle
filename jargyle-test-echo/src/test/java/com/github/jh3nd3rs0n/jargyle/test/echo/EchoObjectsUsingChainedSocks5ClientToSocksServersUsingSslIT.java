package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
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

import static com.github.jh3nd3rs0n.jargyle.test.echo.SocksServersHelper.stopSocksServers;
import static org.junit.Assert.assertEquals;

public class EchoObjectsUsingChainedSocks5ClientToSocksServersUsingSslIT {

    private static DatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static Server echoServer;
    private static int echoServerPort;

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
                                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                                        Boolean.TRUE),
                                SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
                                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString())),
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
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
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
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                SslSettingSpecConstants.SSL_KEY_STORE_INPUT_STREAM.newSetting(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        socksServerPort2UsingSsl = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        return socksServerList;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoDatagramServer = EchoDatagramServerHelper.newEchoDatagramServer(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServerHelper.newEchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServersUsingSsl = newSocksServersUsingSsl();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(DatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(Server.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServersUsingSsl != null) {
            stopSocksServers(socksServersUsingSsl);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServersUsingSsl05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSsl05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl01() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl02() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl03() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl04() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSsl05() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_05;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
