package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.*;
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

	private static DatagramEchoServer datagramEchoServer;
	private static int datagramEchoServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;
	
	private static SocksServer socksServerUsingSsl;
	private static int socksServerPortUsingSsl;
	private static SocksServer socksServerUsingSslWithRequestedClientAuth;
	private static int socksServerPortUsingSslWithRequestedClientAuth;
	private static SocksServer socksServerUsingSslWithRequiredClientAuth;
	private static int socksServerPortUsingSslWithRequiredClientAuth;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.SECONDS)
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
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()))));
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
						Boolean.TRUE))));
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
						TestKeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString()))));
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
		datagramEchoServer = new DatagramEchoServer(0);
		datagramEchoServer.start();
		datagramEchoServerPort = datagramEchoServer.getPort();
		echoServer = new EchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		socksServerUsingSsl = newSocksServerUsingSsl();
		socksServerUsingSslWithRequestedClientAuth =
				newSocksServerUsingSslWithRequestedClientAuth();
		socksServerUsingSslWithRequiredClientAuth =
				newSocksServerUsingSslWithRequiredClientAuth();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		// System.clearProperty("javax.net.debug");
		if (datagramEchoServer != null
				&& !datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
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
		ThreadHelper.interruptableSleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSsl04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSsl04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSsl04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
