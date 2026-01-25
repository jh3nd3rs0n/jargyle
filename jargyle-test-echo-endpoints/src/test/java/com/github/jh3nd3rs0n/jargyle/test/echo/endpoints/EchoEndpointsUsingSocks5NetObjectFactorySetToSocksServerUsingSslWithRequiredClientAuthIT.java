package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
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

public class EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuthIT {

    private static EchoDatagramServer echoDatagramServer;
    private static int echoDatagramServerPort;
    private static EchoServer echoServer;
    private static int echoServerPort;

    private static AbstractSocksServer socksServerUsingSslWithRequiredClientAuth;
    private static int socksServerPortUsingSslWithRequiredClientAuth;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static AbstractSocksServer newSocksServerUsingSslWithRequiredClientAuth() throws IOException {
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
                SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH.newSetting(
                        Boolean.TRUE),
                SslSettingSpecConstants.SSL_TRUST_STORE_BYTES.newSetting(
                        Bytes.of(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes())),
                SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
                SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        socksServerPortUsingSslWithRequiredClientAuth = socksServer.getPort();
        return socksServer;
    }

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth() {
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
                        socksServerPortUsingSslWithRequiredClientAuth)
                .newSocksClient(properties)
                .newSocksNetObjectFactory();
    }

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth() {
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
                        socksServerPortUsingSslWithRequiredClientAuth)
                .newSocksClient(properties)
                .newSocksNetObjectFactory();
    }

    private static NetObjectFactory newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth() {
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
                        socksServerPortUsingSslWithRequiredClientAuth)
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
        socksServerUsingSslWithRequiredClientAuth =
                newSocksServerUsingSslWithRequiredClientAuth();
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
        if (socksServerUsingSslWithRequiredClientAuth != null
                && !socksServerUsingSslWithRequiredClientAuth.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
            socksServerUsingSslWithRequiredClientAuth.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_03;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_04;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                new NetObjectFactoryToDatagramSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_05;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
        EchoClient echoClient = new EchoClient(
                new NetObjectFactoryToSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()));
        String string = StringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithDifferentRequiredClientAuth()), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithNoRequiredClientAuth()), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()), 0);
        String string = StringConstants.STRING_03;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()), 0);
        String string = StringConstants.STRING_04;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                new NetObjectFactoryToServerSocketFactoryAdapter(newSocks5NetObjectFactoryUsingSslWithRequiredClientAuth()), 0);
        String string = StringConstants.STRING_05;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
