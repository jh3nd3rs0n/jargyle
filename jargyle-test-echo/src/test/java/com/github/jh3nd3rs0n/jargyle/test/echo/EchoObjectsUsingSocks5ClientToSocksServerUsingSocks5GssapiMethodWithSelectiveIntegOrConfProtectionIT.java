package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
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

public class EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtectionIT {

    private static DatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static Server echoServer;
    private static int echoServerPort;

    private static SocksServer socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection;
    private static int socksServerPortUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static SocksServer newSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection() throws IOException {
        SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.GSSAPI)),
                Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newSetting(
                        ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
                Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG.newSetting(
                        1),
                Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF.newSetting(
                        Boolean.TRUE),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        socksServerPortUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection =
                socksServer.getPort().intValue();
        return socksServer;
    }

    private static SocksClient newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection() {
        Properties properties = Properties.of(
                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                        Methods.of(Method.GSSAPI)),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_MECHANISM_OID.newPropertyWithParsedValue(
                        GssEnvironment.MECHANISM_OID),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
                        GssEnvironment.SERVICE_NAME),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
                        ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG.newProperty(
                        1),
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF.newProperty(
                        Boolean.TRUE));
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection)
                .newSocksClient(properties);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(
                EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtectionIT.class);
        echoDatagramServer = EchoDatagramServerHelper.newEchoDatagramServer(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServerHelper.newEchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection =
                newSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(
                EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtectionIT.class);
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(DatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(Server.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection != null
                && !socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection.getState().equals(SocksServer.State.STOPPED)) {
            socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection01() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
                        .newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection02() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
                        .newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection03() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
                        .newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection04() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
                        .newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection05() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
                        .newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_05;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
