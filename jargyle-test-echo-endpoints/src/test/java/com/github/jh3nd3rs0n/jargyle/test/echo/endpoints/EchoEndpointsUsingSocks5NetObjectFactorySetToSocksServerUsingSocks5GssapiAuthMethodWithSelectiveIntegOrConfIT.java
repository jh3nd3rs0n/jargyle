package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.test.socks.server.AbstractSocksServer;
import com.github.jh3nd3rs0n.jargyle.test.socks.server.JargyleSocksServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfIT {

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static AbstractSocksServer socksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection;
    private static int socksServerPortUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static AbstractSocksServer newSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection() throws IOException {
        AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.GSSAPI)),
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newSetting(
                        ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newSetting(
                        1),
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newSetting(
                        Boolean.TRUE),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        socksServerPortUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection =
                socksServer.getPort();
        return socksServer;
    }

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                        Methods.of(Method.GSSAPI)),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID.newPropertyWithParsedValue(
                        GssEnvironment.MECHANISM_OID),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
                        GssEnvironment.SERVICE_NAME),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.newProperty(
                        1),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF.newProperty(
                        Boolean.TRUE));
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection)
                .newSocksClient(properties)
                .newSocksNetObjectFactory();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(
                EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfIT.class);
        echoDatagramServer = EchoDatagramServer.newInstance(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection =
                newSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(
                EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfIT.class);
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(EchoDatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection != null
                && !socksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
            socksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection())
                        , 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection())
                        , 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection())
                        , 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection())
                        , 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConf05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtection())
                        , 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
