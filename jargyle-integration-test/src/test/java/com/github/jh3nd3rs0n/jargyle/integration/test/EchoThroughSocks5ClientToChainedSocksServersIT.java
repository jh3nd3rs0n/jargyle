package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.jh3nd3rs0n.jargyle.integration.test.SocksServersHelper.newStartedSocksServers;
import static com.github.jh3nd3rs0n.jargyle.integration.test.SocksServersHelper.stopSocksServers;
import static org.junit.Assert.assertEquals;

public class EchoThroughSocks5ClientToChainedSocksServersIT {

    private static final int CHAINED_SOCKS_SERVER_PORT_1 = 2100;
    private static final int CHAINED_SOCKS_SERVER_PORT_2 = 2200;
    private static final int CHAINED_SOCKS_SERVER_PORT_3 = 2300;

    private static List<SocksServer> chainedSocksServers;

    private static DatagramEchoServer datagramEchoServer;
    private static EchoServer echoServer;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<Configuration> newChainedConfigurations() {
        return Arrays.asList(
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                                Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_1)),
                        ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                                Scheme.SOCKS5.newSocksServerUri(
                                        InetAddress.getLoopbackAddress().getHostAddress(),
                                        CHAINED_SOCKS_SERVER_PORT_2)))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                                Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_2)),
                        ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                                Scheme.SOCKS5.newSocksServerUri(
                                        InetAddress.getLoopbackAddress().getHostAddress(),
                                        CHAINED_SOCKS_SERVER_PORT_3)))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                                Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_3)))));
    }

    private static SocksClient newSocks5ClientToChainedSocksServers() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        CHAINED_SOCKS_SERVER_PORT_1)
                .newSocksClient(Properties.of());
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = new DatagramEchoServer();
        echoServer = new EchoServer();
        datagramEchoServer.start();
        echoServer.start();
        chainedSocksServers = newStartedSocksServers(newChainedConfigurations());
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
            datagramEchoServer.stop();
        }
        if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        stopSocksServers(chainedSocksServers);
        ThreadHelper.sleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers01() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers02() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers03() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
