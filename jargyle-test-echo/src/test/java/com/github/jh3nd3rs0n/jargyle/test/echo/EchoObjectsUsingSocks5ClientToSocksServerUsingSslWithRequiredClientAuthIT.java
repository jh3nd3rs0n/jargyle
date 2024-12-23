package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
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

public class EchoObjectsUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuthIT {

    private static DatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static Server echoServer;
    private static int echoServerPort;

    private static SocksServer socksServerUsingSslWithRequiredClientAuth;
    private static int socksServerPortUsingSslWithRequiredClientAuth;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static SocksServer newSocksServerUsingSslWithRequiredClientAuth() throws IOException {
        SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_INPUT_STREAM.newSetting(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                SslSettingSpecConstants.SSL_KEY_STORE_INPUT_STREAM.newSetting(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH.newSetting(
                        Boolean.TRUE),
                SslSettingSpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newSetting(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream()),
                SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        socksServerPortUsingSslWithRequiredClientAuth = socksServer.getPort().intValue();
        return socksServer;
    }

    private static SocksClient newSocks5ClientUsingSslWithDifferentRequiredClientAuth() {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
                SslPropertySpecConstants.SSL_KEY_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSslWithRequiredClientAuth)
                .newSocksClient(properties);
    }

    private static SocksClient newSocks5ClientUsingSslWithNoRequiredClientAuth() {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                        Boolean.TRUE),
                SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSslWithRequiredClientAuth)
                .newSocksClient(properties);
    }

    private static SocksClient newSocks5ClientUsingSslWithRequiredClientAuth() {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
                SslPropertySpecConstants.SSL_KEY_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream()),
                SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
                SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSslWithRequiredClientAuth)
                .newSocksClient(properties);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        // System.setProperty("javax.net.debug", "ssl,handshake");
        echoDatagramServer = EchoDatagramServerHelper.newEchoDatagramServer(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServerHelper.newEchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServerUsingSslWithRequiredClientAuth =
                newSocksServerUsingSslWithRequiredClientAuth();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        // System.clearProperty("javax.net.debug");
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(DatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(Server.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServerUsingSslWithRequiredClientAuth != null
                && !socksServerUsingSslWithRequiredClientAuth.getState().equals(SocksServer.State.STOPPED)) {
            socksServerUsingSslWithRequiredClientAuth.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_05;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_05;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
        String string = StringConstants.STRING_05;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
