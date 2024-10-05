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

public class EchoThroughChainedSocks5ClientToSocksServersUsingSocks5UserpassMethodIT {

    private static DatagramEchoServer datagramEchoServer;
    private static int datagramEchoServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static List<SocksServer> socksServersUsingSocks5UserpassMethod;
    private static int socksServerPort1UsingSocks5Userpassmethod;
    private static int socksServerPort2UsingSocks5Userpassmethod;
    private static int socksServerPort3UsingSocks5Userpassmethod;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static SocksClient newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod() {
        SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort1UsingSocks5Userpassmethod)
                .newSocksClient(Properties.of(
                        Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                                "Aladdin"),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                                EncryptedPassword.newInstance(
                                        "opensesame".toCharArray()))));
        SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort2UsingSocks5Userpassmethod)
                .newSocksClient(Properties.of(
                                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                        Methods.of(Method.USERNAME_PASSWORD)),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                                        "Jasmine"),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                                        EncryptedPassword.newInstance(
                                                "mission:impossible".toCharArray()))),
                        client1);
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort3UsingSocks5Userpassmethod)
                .newSocksClient(Properties.of(
                                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                        Methods.of(Method.USERNAME_PASSWORD)),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                                        "Abu"),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                                        EncryptedPassword.newInstance(
                                                "safeDriversSave40%".toCharArray()))),
                        client2);
    }

    private static List<SocksServer> newSocksServersUsingSocks5UserpassMethod() throws IOException {
        List<SocksServer> socksServerList = new ArrayList<>();
        SocksServer socksServer1 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Aladdin:opensesame")))));
        socksServer1.start();
        socksServerPort1UsingSocks5Userpassmethod = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        SocksServer socksServer2 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Jasmine:mission%3Aimpossible")))));
        socksServer2.start();
        socksServerPort2UsingSocks5Userpassmethod = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
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
        socksServerPort3UsingSocks5Userpassmethod = socksServer3.getPort().intValue();
        socksServerList.add(socksServer3);
        return socksServerList;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = new DatagramEchoServer(0);
        datagramEchoServer.start();
        datagramEchoServerPort = datagramEchoServer.getPort();
        echoServer = new EchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServersUsingSocks5UserpassMethod =
                newSocksServersUsingSocks5UserpassMethod();
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
        if (socksServersUsingSocks5UserpassMethod != null) {
            stopSocksServers(socksServersUsingSocks5UserpassMethod);
        }
        ThreadHelper.interruptableSleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod04() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
