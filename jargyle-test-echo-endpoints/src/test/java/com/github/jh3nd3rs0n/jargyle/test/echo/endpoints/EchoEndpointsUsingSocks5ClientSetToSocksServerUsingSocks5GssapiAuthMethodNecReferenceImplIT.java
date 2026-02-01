package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplIT {

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static AbstractSocksServer socksServerUsingSocks5GssapiAuthMethodNecReferenceImpl;
    private static int socksServerPortUsingSocks5GssapiAuthMethodNecReferenceImpl;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static AbstractSocksServer newSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl() throws IOException {
        AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.GSSAPI)),
                Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newSetting(
                        Boolean.TRUE),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        socksServerPortUsingSocks5GssapiAuthMethodNecReferenceImpl = socksServer.getPort();
        return socksServer;
    }

    private static SocksClient newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
            final ProtectionLevels protectionLevels) {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                        Methods.of(Method.GSSAPI)),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID.newPropertyWithParsedValue(
                        GssEnvironment.MECHANISM_OID),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
                        GssEnvironment.SERVICE_NAME),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.newProperty(
                        Boolean.TRUE),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
                        protectionLevels));
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSocks5GssapiAuthMethodNecReferenceImpl)
                .newSocksClient(properties);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(
                EchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplIT.class);
        echoDatagramServer = EchoDatagramServer.newInstance(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServerUsingSocks5GssapiAuthMethodNecReferenceImpl =
                newSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(
                EchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplIT.class);
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(EchoDatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServerUsingSocks5GssapiAuthMethodNecReferenceImpl != null
                && !socksServerUsingSocks5GssapiAuthMethodNecReferenceImpl.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
            socksServerUsingSocks5GssapiAuthMethodNecReferenceImpl.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new SocksDatagramSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new SocksSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImpl05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(ProtectionLevel.NONE))), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithIntegAndConf05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG_AND_CONF))), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithInteg05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new SocksServerSocketFactory(newSocks5ClientUsingSocks5GssapiAuthMethodNecReferenceImpl(
                        ProtectionLevels.of(
                                ProtectionLevel.REQUIRED_INTEG))), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
