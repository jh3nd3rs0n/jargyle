package com.github.jh3nd3rs0n.echo;

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
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;
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

import static com.github.jh3nd3rs0n.echo.SocksServersHelper.newStartedSocksServers;
import static com.github.jh3nd3rs0n.echo.SocksServersHelper.stopSocksServers;
import static org.junit.Assert.assertEquals;

public class EchoThroughSocks5ClientToChainedSocksServersUsingSocks5UserpassMethodIT {

    private static final int CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSMETHOD = 3100;
    private static final int CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSMETHOD = 3200;
    private static final int CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSMETHOD = 3300;

    private static List<SocksServer> chainedSocksServersUsingSocks5UserpassMethod;

    private static DatagramEchoServer datagramEchoServer;
    private static EchoServer echoServer;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<Configuration> newChainedConfigurationsUsingSocks5UserpassMethod() {
        return Arrays.asList(
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                                Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSMETHOD)),
                        Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                                UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                        "Aladdin:opensesame")),
                        ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                                Scheme.SOCKS5.newSocksServerUri(
                                        InetAddress.getLoopbackAddress().getHostAddress(),
                                        CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSMETHOD)),
                        ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_USERNAME.newSetting(
                                "Jasmine"),
                        ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD.newSetting(
                                EncryptedPassword.newInstance("mission:impossible".toCharArray())))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                                Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSMETHOD)),
                        Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                                UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                        "Jasmine:mission%3Aimpossible")),
                        ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                                Scheme.SOCKS5.newSocksServerUri(
                                        InetAddress.getLoopbackAddress().getHostAddress(),
                                        CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSMETHOD)),
                        ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_USERNAME.newSetting(
                                "Abu"),
                        ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD.newSetting(
                                EncryptedPassword.newInstance("safeDriversSave40%".toCharArray())))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                                Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSMETHOD)),
                        Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                                UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                        "Abu:safeDriversSave40%25")))));
    }

    private static SocksClient newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSMETHOD)
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
        datagramEchoServer = new DatagramEchoServer();
        echoServer = new EchoServer();
        datagramEchoServer.start();
        echoServer.start();
        chainedSocksServersUsingSocks5UserpassMethod = newStartedSocksServers(
                newChainedConfigurationsUsingSocks5UserpassMethod());
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
            datagramEchoServer.stop();
        }
        if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        stopSocksServers(chainedSocksServersUsingSocks5UserpassMethod);
        ThreadHelper.sleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string);
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

}
