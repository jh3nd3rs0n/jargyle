package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
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

public class EchoObjectsUsingChainedSocks5ClientToSocksServersIT {


    private static DatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static Server echoServer;
    private static int echoServerPort;

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
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        socksServerPort1 = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        SocksServer socksServer2 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        socksServerPort2 = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        SocksServer socksServer3 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        socksServerPort3 = socksServer3.getPort().intValue();
        socksServerList.add(socksServer3);
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
        socksServers = newSocksServers();
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
        if (socksServers != null) {
            stopSocksServers(socksServers);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServers01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServers02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServers03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServers04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5ClientToSocksServers05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServers01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServers02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServers03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServers04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServers05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServers01() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServers02() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServers03() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServers04() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServers05() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newChainedSocks5ClientToSocksServers().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_05;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
