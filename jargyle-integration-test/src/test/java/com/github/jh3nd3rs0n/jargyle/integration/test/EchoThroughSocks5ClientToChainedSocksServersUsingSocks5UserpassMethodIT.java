package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
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

public class EchoThroughSocks5ClientToChainedSocksServersUsingSocks5UserpassMethodIT {

    private static List<SocksServer> chainedSocksServersUsingSocks5UserpassMethod;
    private static int chainedSocksServerPort1UsingSocks5UserpassMethod;

    private static DatagramTestServer echoDatagramTestServer;
    private static int echoDatagramTestServerPort;
    private static TestServer echoTestServer;
    private static int echoTestServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
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
                                "Abu:safeDriversSave40%25")),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        int chainedSocksServerPort3UsingSocks5UserpassMethod = socksServer3.getPort().intValue();
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
                                chainedSocksServerPort3UsingSocks5UserpassMethod)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_USERNAME.newSetting(
                        "Abu"),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD.newSetting(
                        EncryptedPassword.newInstance("safeDriversSave40%".toCharArray())),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        int chainedSocksServerPort2UsingSocks5UserpassMethod = socksServer2.getPort().intValue();
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
                                chainedSocksServerPort2UsingSocks5UserpassMethod)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_USERNAME.newSetting(
                        "Jasmine"),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD.newSetting(
                        EncryptedPassword.newInstance("mission:impossible".toCharArray())),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        chainedSocksServerPort1UsingSocks5UserpassMethod = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        return socksServerList;
    }

    private static SocksClient newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        chainedSocksServerPort1UsingSocks5UserpassMethod)
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
        echoDatagramTestServer = EchoDatagramTestServerHelper.newEchoDatagramTestServer(0);
        echoDatagramTestServer.start();
        echoDatagramTestServerPort = echoDatagramTestServer.getPort();
        echoTestServer = EchoTestServerHelper.newEchoTestServer(0);
        echoTestServer.start();
        echoTestServerPort = echoTestServer.getPort();
        chainedSocksServersUsingSocks5UserpassMethod =
                newChainedSocksServersUsingSocks5UserpassMethod();
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
        if (chainedSocksServersUsingSocks5UserpassMethod != null) {
            stopSocksServers(chainedSocksServersUsingSocks5UserpassMethod);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod05() throws IOException {
        EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod05() throws IOException {
        EchoTestClient echoTestClient = new EchoTestClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoTestClient.echo(string, echoTestServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoTestServerUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod05() throws IOException {
        TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_05;
        String returningString = EchoTestServerHelper.startThenEchoThenStop(
                echTestServer, new EchoTestClient(), string);
        assertEquals(string, returningString);
    }

}
