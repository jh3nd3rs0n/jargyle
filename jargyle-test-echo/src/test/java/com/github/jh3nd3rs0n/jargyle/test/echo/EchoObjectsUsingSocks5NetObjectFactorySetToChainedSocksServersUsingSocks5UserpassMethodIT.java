package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
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

public class EchoObjectsUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethodIT {

    private static List<AbstractSocksServer> chainedSocksServersUsingSocks5UserpassMethod;
    private static int chainedSocksServerPort1UsingSocks5UserpassMethod;

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<AbstractSocksServer> newChainedSocksServersUsingSocks5UserpassMethod() throws IOException {
        List<AbstractSocksServer> socksServerList = new ArrayList<>();
        AbstractSocksServer socksServer3 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Abu:safeDriversSave40%25")),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        int chainedSocksServerPort3UsingSocks5UserpassMethod = socksServer3.getPort();
        socksServerList.add(socksServer3);
        AbstractSocksServer socksServer2 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
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
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        int chainedSocksServerPort2UsingSocks5UserpassMethod = socksServer2.getPort();
        socksServerList.add(socksServer2);
        AbstractSocksServer socksServer1 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
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
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        chainedSocksServerPort1UsingSocks5UserpassMethod = socksServer1.getPort();
        socksServerList.add(socksServer1);
        return socksServerList;
    }

    private static NetObjectFactory newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod() {
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
                                        "opensesame".toCharArray()))))
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
        chainedSocksServersUsingSocks5UserpassMethod =
                newChainedSocksServersUsingSocks5UserpassMethod();
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
        if (chainedSocksServersUsingSocks5UserpassMethod != null) {
            stopSocksServers(chainedSocksServersUsingSocks5UserpassMethod);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod(), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod(), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactorySetToChainedSocksServersUsingSocks5UserpassMethod(), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
