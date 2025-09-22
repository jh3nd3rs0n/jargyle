package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
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
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerIT {

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
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
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

    private static NetObjectFactory newSocks5NetObjectFactorySetToBogusSocksServer() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        BOGUS_SOCKS_SERVER_PORT)
                .newSocksClient(Properties.of())
                .newSocksNetObjectFactory();
    }

    private static NetObjectFactory newSocks5NetObjectFactorySetToJargyleSocksServer() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        jargyleSocksServerPort)
                .newSocksClient(Properties.of())
                .newSocksNetObjectFactory();
    }

    private static NetObjectFactory newSocks5NetObjectFactorySetToNettySocksServer() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        nettySocksServerPort)
                .newSocksClient(Properties.of())
                .newSocksNetObjectFactory();
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
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToBogusSocksServerForIOException() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToBogusSocksServer());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToJargyleSocksServer01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToJargyleSocksServer02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToJargyleSocksServer03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToJargyleSocksServer04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToJargyleSocksServer05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToBogusSocksServerForIOException01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToBogusSocksServer());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToJargyleSocksServer01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToJargyleSocksServer02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToJargyleSocksServer03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToJargyleSocksServer04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToJargyleSocksServer05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToJargyleSocksServer());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToNettySocksServer01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToNettySocksServer());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToNettySocksServer02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToNettySocksServer());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToNettySocksServer03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToNettySocksServer());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToNettySocksServer04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToNettySocksServer());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToNettySocksServer05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToNettySocksServer());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToBogusSocksServerForIOException01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToBogusSocksServer(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToJargyleSocksServer01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToJargyleSocksServer(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToJargyleSocksServer02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToJargyleSocksServer(), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToJargyleSocksServer03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToJargyleSocksServer(), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToJargyleSocksServer04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToJargyleSocksServer(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToJargyleSocksServer05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToJargyleSocksServer(), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
