package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.TestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.security.TestKeyStoreResourceConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
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

public class EchoThroughSocks5ClientToSocksServerUsingSslIT {

	private static DatagramTestServer echoDatagramTestServer;
	private static int echoDatagramTestServerPort;
	private static TestServer echoTestServer;
	private static int echoTestServerPort;
	
	private static SocksServer socksServerUsingSsl;
	private static int socksServerPortUsingSsl;
	private static SocksServer socksServerUsingSslWithRequestedClientAuth;
	private static int socksServerPortUsingSslWithRequestedClientAuth;
	private static SocksServer socksServerUsingSslWithRequiredClientAuth;
	private static int socksServerPortUsingSslWithRequiredClientAuth;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static SocksServer newSocksServerUsingSsl() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSsl = socksServer.getPort().intValue();
		return socksServer;
	}
	
	private static SocksServer newSocksServerUsingSslWithRequestedClientAuth() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslSettingSpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream()),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
				SslSettingSpecConstants.SSL_WANT_CLIENT_AUTH.newSetting(
						Boolean.TRUE),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSslWithRequestedClientAuth = socksServer.getPort().intValue();
		return socksServer;
	}
	
	private static SocksServer newSocksServerUsingSslWithRequiredClientAuth() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH.newSetting(
						Boolean.TRUE),
				SslSettingSpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newSetting(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream()),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSslWithRequiredClientAuth = socksServer.getPort().intValue();
		return socksServer;
	}

	private static SocksClient newSocks5ClientUsingSsl() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSsl)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithDifferentRequestedClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSslWithRequestedClientAuth)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithDifferentRequiredClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSslWithRequiredClientAuth)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithNoRequestedClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSslWithRequestedClientAuth)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithNoRequiredClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSslWithRequiredClientAuth)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithRequestedClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSslWithRequestedClientAuth)
				.newSocksClient(properties);
	}
	
	private static SocksClient newSocks5ClientUsingSslWithRequiredClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getInputStream()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSslWithRequiredClientAuth)
				.newSocksClient(properties);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		// System.setProperty("javax.net.debug", "ssl,handshake");
		echoDatagramTestServer = EchoDatagramTestServerHelper.newEchoDatagramTestServer(0);
		echoDatagramTestServer.start();
		echoDatagramTestServerPort = echoDatagramTestServer.getPort();
		echoTestServer = EchoTestServerHelper.newEchoTestServer(0);
		echoTestServer.start();
		echoTestServerPort = echoTestServer.getPort();
		socksServerUsingSsl = newSocksServerUsingSsl();
		socksServerUsingSslWithRequestedClientAuth =
				newSocksServerUsingSslWithRequestedClientAuth();
		socksServerUsingSslWithRequiredClientAuth =
				newSocksServerUsingSslWithRequiredClientAuth();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		// System.clearProperty("javax.net.debug");
		if (echoDatagramTestServer != null
				&& !echoDatagramTestServer.getState().equals(DatagramTestServer.State.STOPPED)) {
			echoDatagramTestServer.stop();
		}
		if (echoTestServer != null
				&& !echoTestServer.getState().equals(TestServer.State.STOPPED)) {
			echoTestServer.stop();
		}
		if (socksServerUsingSsl != null
				&& !socksServerUsingSsl.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSsl.stop();
		}
		if (socksServerUsingSslWithRequestedClientAuth != null
				&& !socksServerUsingSslWithRequestedClientAuth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSslWithRequestedClientAuth.stop();
		}
		if (socksServerUsingSslWithRequiredClientAuth != null
				&& !socksServerUsingSslWithRequiredClientAuth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSslWithRequiredClientAuth.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSsl04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSsl05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSsl04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSsl05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSsl04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSsl05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

}
