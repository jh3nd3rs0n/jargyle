package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.UserRepositorySpecConstants;
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

public class EchoObjectsUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethodIT {

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static List<AbstractSocksServer> socksServersUsingSocks5UserpassAuthMethod;
    private static int socksServerPort1UsingSocks5UserpassAuthMethod;
    private static int socksServerPort2UsingSocks5UserpassAuthMethod;
    private static int socksServerPort3UsingSocks5UserpassAuthMethod;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static NetObjectFactory newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod() {
        SocksClient client1 = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort1UsingSocks5UserpassAuthMethod)
                .newSocksClient(Properties.of(
                        Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME.newProperty(
                                "Aladdin"),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                                EncryptedPassword.newInstance(
                                        "opensesame".toCharArray()))));
        SocksClient client2 = SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort2UsingSocks5UserpassAuthMethod)
                .newSocksClient(Properties.of(
                                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                        Methods.of(Method.USERNAME_PASSWORD)),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME.newProperty(
                                        "Jasmine"),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                                        EncryptedPassword.newInstance(
                                                "mission:impossible".toCharArray()))),
                        client1);
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPort3UsingSocks5UserpassAuthMethod)
                .newSocksClient(Properties.of(
                                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                        Methods.of(Method.USERNAME_PASSWORD)),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME.newProperty(
                                        "Abu"),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                                        EncryptedPassword.newInstance(
                                                "safeDriversSave40%".toCharArray()))),
                        client2)
                .newSocksNetObjectFactory();
    }

    private static List<AbstractSocksServer> newSocksServersUsingSocks5UserpassAuthMethod() throws IOException {
        List<AbstractSocksServer> socksServerList = new ArrayList<>();
        AbstractSocksServer socksServer1 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Aladdin:opensesame")),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        socksServerPort1UsingSocks5UserpassAuthMethod = socksServer1.getPort();
        socksServerList.add(socksServer1);
        AbstractSocksServer socksServer2 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Jasmine:mission%3Aimpossible")),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        socksServerPort2UsingSocks5UserpassAuthMethod = socksServer2.getPort();
        socksServerList.add(socksServer2);
        AbstractSocksServer socksServer3 = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Abu:safeDriversSave40%25")),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        socksServerPort3UsingSocks5UserpassAuthMethod = socksServer3.getPort();
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
        socksServersUsingSocks5UserpassAuthMethod =
                newSocksServersUsingSocks5UserpassAuthMethod();
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
        if (socksServersUsingSocks5UserpassAuthMethod != null) {
            stopSocksServers(socksServersUsingSocks5UserpassAuthMethod);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod(), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod(), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newChainedSocks5NetObjectFactorySetToSocksServersUsingSocks5UserpassAuthMethod(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
