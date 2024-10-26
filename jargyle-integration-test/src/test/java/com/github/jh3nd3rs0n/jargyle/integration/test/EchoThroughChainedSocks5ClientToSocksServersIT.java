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

public class EchoThroughChainedSocks5ClientToSocksServersIT {


    private static DatagramTestServer echoDatagramTestServer;
    private static int echoDatagramTestServerPort;
    private static TestServer echoTestServer;
    private static int echoTestServerPort;

    private static List<SocksServer> socksServers;
    private static int socksServerPort1;
    private static int socksServerPort2;
    private static int socksServerPort3;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();


    private static SocksClient newChainedSocks5ClientToSocksServers() {
        SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort1)
                .newSocksClient(Properties.of());
        SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort2)
                .newSocksClient(Properties.of(), client1);
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort3)
                .newSocksClient(Properties.of(), client2);
    }

    private static List<SocksServer> newSocksServers() throws IOException {
        List<SocksServer> socksServerList = new ArrayList<>();
        SocksServer socksServer1 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        socksServerPort1 = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        SocksServer socksServer2 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        socksServerPort2 = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        SocksServer socksServer3 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        socksServerPort3 = socksServer3.getPort().intValue();
        socksServerList.add(socksServer3);
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
        socksServers = newSocksServers();
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
        if (socksServers != null) {
            stopSocksServers(socksServers);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServers01() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServers02() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServers03() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServers04() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingChainedSocks5ClientToSocksServers05() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServers01() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServers02() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServers03() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServers04() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingChainedSocks5ClientToSocksServers05() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServers01() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServers02() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServers03() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServers04() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingChainedSocks5ClientToSocksServers05() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_05;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

}
