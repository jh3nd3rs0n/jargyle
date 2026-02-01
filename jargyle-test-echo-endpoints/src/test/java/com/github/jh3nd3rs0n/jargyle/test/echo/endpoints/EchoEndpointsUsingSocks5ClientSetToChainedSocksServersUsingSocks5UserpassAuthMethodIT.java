package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;
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

import static com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.AbstractSocksServer.stop;
import static org.junit.Assert.assertEquals;

public class EchoEndpointsUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethodIT {

    private static List<AbstractSocksServer> chainedSocksServersUsingSocks5UserpassAuthMethod;
    private static int chainedSocksServerPort1UsingSocks5UserpassAuthMethod;

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<AbstractSocksServer> newChainedSocksServersUsingSocks5UserpassAuthMethod() throws IOException {
        List<AbstractSocksServer> socksServerList = new ArrayList<>();
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
        int chainedSocksServerPort3UsingSocks5UserpassAuthMethod = socksServer3.getPort();
        socksServerList.add(socksServer3);
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
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        SocksServerUriScheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort3UsingSocks5UserpassAuthMethod)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTHMETHOD_USERNAME.newSetting(
                        "Abu"),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newSetting(
                        EncryptedPassword.newInstance("safeDriversSave40%".toCharArray())),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        int chainedSocksServerPort2UsingSocks5UserpassAuthMethod = socksServer2.getPort();
        socksServerList.add(socksServer2);
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
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        SocksServerUriScheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort2UsingSocks5UserpassAuthMethod)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTHMETHOD_USERNAME.newSetting(
                        "Jasmine"),
                ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newSetting(
                        EncryptedPassword.newInstance("mission:impossible".toCharArray())),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        chainedSocksServerPort1UsingSocks5UserpassAuthMethod = socksServer1.getPort();
        socksServerList.add(socksServer1);
        return socksServerList;
    }

    private static SocksClient newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod() {
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        chainedSocksServerPort1UsingSocks5UserpassAuthMethod)
                .newSocksClient(Properties.of(
                        Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME.newProperty(
                                "Aladdin"),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD.newProperty(
                                EncryptedPassword.newInstance(
                                        "opensesame".toCharArray()))));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoDatagramServer = EchoDatagramServer.newInstance(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        chainedSocksServersUsingSocks5UserpassAuthMethod =
                newChainedSocksServersUsingSocks5UserpassAuthMethod();
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
        if (chainedSocksServersUsingSocks5UserpassAuthMethod != null) {
            stop(chainedSocksServersUsingSocks5UserpassAuthMethod);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientSetToChainedSocksServersUsingSocks5UserpassAuthMethod()), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
