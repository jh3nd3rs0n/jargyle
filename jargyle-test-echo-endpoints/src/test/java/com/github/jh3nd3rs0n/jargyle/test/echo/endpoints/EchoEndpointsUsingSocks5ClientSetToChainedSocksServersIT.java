package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
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

import static com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.AbstractSocksServer.stop;
import static org.junit.Assert.assertEquals;

public class EchoEndpointsUsingSocks5ClientSetToChainedSocksServersIT {

    private static List<AbstractSocksServer> chainedSocksServers;
    private static int chainedSocksServerPort1;

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<AbstractSocksServer> newChainedSocksServers() throws IOException {
        List<AbstractSocksServer> socksServerList = new ArrayList<>();
        AbstractSocksServer socksServer3 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        int chainedSocksServerPort3 = socksServer3.getPort();
        socksServerList.add(socksServer3);
        AbstractSocksServer socksServer2 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        SocksServerUriScheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort3)),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        int chainedSocksServerPort2 = socksServer2.getPort();
        socksServerList.add(socksServer2);
        AbstractSocksServer socksServer1 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        SocksServerUriScheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort2)),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        chainedSocksServerPort1 = socksServer1.getPort();
        socksServerList.add(socksServer1);
        return socksServerList;
    }

    private static SocksClient newSocks5ClientSetToChainedSocksServers() {
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        chainedSocksServerPort1)
                .newSocksClient(Properties.of());
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoDatagramServer = EchoDatagramServer.newInstance(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        chainedSocksServers = newChainedSocksServers();
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
        if (chainedSocksServers != null) {
            stop(chainedSocksServers);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServers01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServers02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServers03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServers04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServers05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServers01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServers02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServers03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServers04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServers05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServers()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServers01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServers()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServers02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServers()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServers03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServers()), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServers04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServers()), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServers05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServers()), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
