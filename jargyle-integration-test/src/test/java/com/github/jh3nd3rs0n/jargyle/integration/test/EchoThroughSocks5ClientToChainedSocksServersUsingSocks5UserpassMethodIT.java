package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
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

public class EchoThroughSocks5ClientToChainedSocksServersUsingSocks5UserpassMethodIT {

    private static List<SocksServer> chainedSocksServersUsingSocks5UserpassMethod;
    private static int chainedSocksServerPort1UsingSocks5Userpassmethod;

    private static DatagramEchoServer datagramEchoServer;
    private static int datagramEchoServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<SocksServer> newChainedSocksServersUsingSocks5UserpassMethod() throws IOException {
        List<SocksServer> socksServerList = new ArrayList<>();
        SocksServer socksServer3 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Abu:safeDriversSave40%25")))));
        socksServer3.start();
        int chainedSocksServerPort3UsingSocks5Userpassmethod = socksServer3.getPort().intValue();
        socksServerList.add(socksServer3);
        SocksServer socksServer2 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Jasmine:mission%3Aimpossible")),
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        Scheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort3UsingSocks5Userpassmethod)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_USERNAME.newSetting(
                        "Abu"),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD.newSetting(
                        EncryptedPassword.newInstance("safeDriversSave40%".toCharArray())))));
        socksServer2.start();
        int chainedSocksServerPort2UsingSocks5Userpassmethod = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        SocksServer socksServer1 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Aladdin:opensesame")),
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        Scheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort2UsingSocks5Userpassmethod)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_USERNAME.newSetting(
                        "Jasmine"),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD.newSetting(
                        EncryptedPassword.newInstance("mission:impossible".toCharArray())))));
        socksServer1.start();
        chainedSocksServerPort1UsingSocks5Userpassmethod = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        return socksServerList;
    }

    private static SocksClient newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        chainedSocksServerPort1UsingSocks5Userpassmethod)
                .newSocksClient(Properties.of(
                        Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                                "Aladdin"),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                                EncryptedPassword.newInstance(
                                        "opensesame".toCharArray()))));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = new DatagramEchoServer(0);
        datagramEchoServer.start();
        datagramEchoServerPort = datagramEchoServer.getPort();
        echoServer = new EchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        chainedSocksServersUsingSocks5UserpassMethod =
                newChainedSocksServersUsingSocks5UserpassMethod();
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
        if (chainedSocksServersUsingSocks5UserpassMethod != null) {
            stopSocksServers(chainedSocksServersUsingSocks5UserpassMethod);
        }
        ThreadHelper.interruptableSleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
