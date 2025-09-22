package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
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

public class EchoObjectsUsingChainedSocks5NetObjectFactorySetToSocksServersIT {


    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static List<AbstractSocksServer> socksServers;
    private static int socksServerPort1;
    private static int socksServerPort2;
    private static int socksServerPort3;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();


    private static NetObjectFactory newChainedSocks5NetObjectFactorySetToSocksServers() {
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
                .newSocksClient(Properties.of(), client2)
                .newSocksNetObjectFactory();
    }

    private static List<AbstractSocksServer> newSocksServers() throws IOException {
        List<AbstractSocksServer> socksServerList = new ArrayList<>();
        AbstractSocksServer socksServer1 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        socksServerPort1 = socksServer1.getPort();
        socksServerList.add(socksServer1);
        AbstractSocksServer socksServer2 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        socksServerPort2 = socksServer2.getPort();
        socksServerList.add(socksServer2);
        AbstractSocksServer socksServer3 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        socksServerPort3 = socksServer3.getPort();
        socksServerList.add(socksServer3);
        return socksServerList;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoDatagramServer = EchoDatagramServer.newInstance(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServers = newSocksServers();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(EchoDatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServers != null) {
            stopSocksServers(socksServers);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServers01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServers02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServers03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServers04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServers05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServers01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServers02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServers03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServers04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServers05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServers());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServers01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServers(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServers02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServers(), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServers03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServers(), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServers04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServers(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServers05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServers(), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
