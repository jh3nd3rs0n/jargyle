package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
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

public class EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuthIT {

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static AbstractSocksServer socksServerUsingSslWithRequestedClientAuth;
    private static int socksServerPortUsingSslWithRequestedClientAuth;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static AbstractSocksServer newSocksServerUsingSslWithRequestedClientAuth() throws IOException {
        AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_BYTES.newSetting(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                SslSettingSpecConstants.SSL_KEY_STORE_BYTES.newSetting(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_TRUST_STORE_BYTES.newSetting(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes())),
                SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
                SslSettingSpecConstants.SSL_WANT_CLIENT_AUTH.newSetting(
                        Boolean.TRUE),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        socksServerPortUsingSslWithRequestedClientAuth = socksServer.getPort();
        return socksServer;
    }

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth() {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
                SslPropertySpecConstants.SSL_KEY_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSslWithRequestedClientAuth)
                .newSocksClient(properties)
                .newSocksNetObjectFactory();
    }

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth() {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                        Boolean.TRUE),
                SslPropertySpecConstants.SSL_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSslWithRequestedClientAuth)
                .newSocksClient(properties)
                .newSocksNetObjectFactory();
    }

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth() {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
                SslPropertySpecConstants.SSL_KEY_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes())),
                SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
                SslPropertySpecConstants.SSL_TRUST_STORE_BYTES.newProperty(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes())),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
        return SocksServerUriScheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        socksServerPortUsingSslWithRequestedClientAuth)
                .newSocksClient(properties)
                .newSocksNetObjectFactory();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        // System.setProperty("javax.net.debug", "ssl,handshake");
        echoDatagramServer = EchoDatagramServer.newInstance(0);
        echoDatagramServer.start();
        echoDatagramServerPort = echoDatagramServer.getPort();
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServerUsingSslWithRequestedClientAuth =
                newSocksServerUsingSslWithRequestedClientAuth();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        // System.clearProperty("javax.net.debug");
        if (echoDatagramServer != null
                && !echoDatagramServer.getState().equals(EchoDatagramServer.State.STOPPED)) {
            echoDatagramServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServerUsingSslWithRequestedClientAuth != null
                && !socksServerUsingSslWithRequestedClientAuth.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
            socksServerUsingSslWithRequestedClientAuth.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth());
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth(), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth(), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequestedClientAuth05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithDifferentRequestedClientAuth(), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth(), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth(), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequestedClientAuth05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithNoRequestedClientAuth(), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth(), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth(), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth(), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth(), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequestedClientAuth05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSslWithRequestedClientAuth(), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
