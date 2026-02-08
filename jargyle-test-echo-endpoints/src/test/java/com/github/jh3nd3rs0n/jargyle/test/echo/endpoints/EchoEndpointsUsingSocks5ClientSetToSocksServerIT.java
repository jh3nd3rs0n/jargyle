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
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoEndpointsUsingSocks5ClientSetToSocksServerIT {

    private static final int BOGUS_SOCKS_SERVER_PORT = 1234;
    private static int jargyleSocksServerPort;
    private static int nettySocksServerPort;

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static AbstractSocksServer jargyleSocksServer;
    private static AbstractSocksServer nettySocksServer;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static AbstractSocksServer newJargyleSocksServer() throws IOException {
        AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        jargyleSocksServerPort = socksServer.getPort();
        return socksServer;
    }

    private static AbstractSocksServer newNettySocksServer() throws IOException {
        AbstractSocksServer socksServer = new NettySocksServer();
        socksServer.start();
        nettySocksServerPort = socksServer.getPort();
        return socksServer;
    }

    private static SocksClient newSocks5ClientSetToBogusSocksServer() {
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        BOGUS_SOCKS_SERVER_PORT)
                .newSocksClient(Properties.of());
    }

    private static SocksClient newSocks5ClientSetToJargyleSocksServer() {
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        jargyleSocksServerPort)
                .newSocksClient(Properties.of());
    }

    private static SocksClient newSocks5ClientSetToNettySocksServer() {
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        nettySocksServerPort)
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
        jargyleSocksServer = newJargyleSocksServer();
        nettySocksServer = newNettySocksServer();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(
                EchoDatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        if (jargyleSocksServer != null
                && !jargyleSocksServer.getState().equals(
                AbstractSocksServer.State.STOPPED)) {
            jargyleSocksServer.stop();
        }
        if (nettySocksServer != null
                && !nettySocksServer.getState().equals(
                AbstractSocksServer.State.STOPPED)) {
            nettySocksServer.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientSetToBogusSocksServerForIOException() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksClientToDatagramSocketFactoryAdapter(newSocks5ClientSetToBogusSocksServer()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToJargyleSocksServer01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksClientToDatagramSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToJargyleSocksServer02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksClientToDatagramSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToJargyleSocksServer03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksClientToDatagramSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToJargyleSocksServer04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksClientToDatagramSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToJargyleSocksServer05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksClientToDatagramSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientSetToBogusSocksServerForIOException01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToBogusSocksServer()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToJargyleSocksServer01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToJargyleSocksServer02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToJargyleSocksServer03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToJargyleSocksServer04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToJargyleSocksServer05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToNettySocksServer01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToNettySocksServer()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToNettySocksServer02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToNettySocksServer()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToNettySocksServer03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToNettySocksServer()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToNettySocksServer04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToNettySocksServer()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToNettySocksServer05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksClientToSocketFactoryAdapter(newSocks5ClientSetToNettySocksServer()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientSetToBogusSocksServerForIOException01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksClientToServerSocketFactoryAdapter(newSocks5ClientSetToBogusSocksServer()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToJargyleSocksServer01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksClientToServerSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToJargyleSocksServer02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksClientToServerSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToJargyleSocksServer03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksClientToServerSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToJargyleSocksServer04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksClientToServerSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToJargyleSocksServer05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksClientToServerSocketFactoryAdapter(newSocks5ClientSetToJargyleSocksServer()), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
