package com.github.jh3nd3rs0n.jargyle.test.echo;

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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoClientUsingProxiedNetObjectFactorySetToSocksServerIT {

    private static EchoServer echoServer;
    private static int echoServerPort;

    private static AbstractSocksServer jargyleSocksServer;
    private static AbstractSocksServer nettySocksServer;

    private static Proxy jargyleProxy;
    private static Proxy nettyProxy;

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
        jargyleProxy = new Proxy(
                Proxy.Type.SOCKS,
                new InetSocketAddress(
                        socksServer.getInetAddress(),
                        socksServer.getPort()));
        return socksServer;
    }

    private static AbstractSocksServer newNettySocksServer() throws IOException {
        AbstractSocksServer socksServer = new NettySocksServer();
        socksServer.start();
        nettyProxy = new Proxy(
                Proxy.Type.SOCKS,
                new InetSocketAddress(
                        socksServer.getInetAddress(),
                        socksServer.getPort()));
        return socksServer;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        jargyleSocksServer = newJargyleSocksServer();
        nettySocksServer = newNettySocksServer();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
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
    public void testEchoClientUsingProxiedNetObjectFactorySetToBogusSocksServerForIOException() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(new Proxy(
                        Proxy.Type.SOCKS,
                        new InetSocketAddress(InetAddress.getLoopbackAddress(), 2345))));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToJargyleSocksServer01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(jargyleProxy));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToJargyleSocksServer02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(jargyleProxy));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToJargyleSocksServer03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(jargyleProxy));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToJargyleSocksServer04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(jargyleProxy));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToJargyleSocksServer05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(jargyleProxy));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToNettySocksServer01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(nettyProxy));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToNettySocksServer02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(nettyProxy));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToNettySocksServer03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(nettyProxy));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToNettySocksServer04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(nettyProxy));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToNettySocksServer05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new ProxiedNetObjectFactory(nettyProxy));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

}
