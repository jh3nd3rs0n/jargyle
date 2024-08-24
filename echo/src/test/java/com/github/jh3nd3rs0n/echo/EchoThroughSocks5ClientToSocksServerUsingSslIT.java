package com.github.jh3nd3rs0n.echo;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;
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
	
	private static final int SOCKS_SERVER_PORT_USING_SSL = 9100;
	private static final int SOCKS_SERVER_PORT_USING_SSL_WITH_REQUESTED_CLIENT_AUTH = 9200;
	private static final int SOCKS_SERVER_PORT_USING_SSL_WITH_REQUIRED_CLIENT_AUTH = 9300;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;
	
	private static SocksServer socksServerUsingSsl;
	private static SocksServer socksServerUsingSslWithRequestedClientAuth;
	private static SocksServer socksServerUsingSslWithRequiredClientAuth;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static Configuration newConfigurationUsingSsl() {
		return Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(SOCKS_SERVER_PORT_USING_SSL)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString())));
	}
	
	private static Configuration newConfigurationUsingSslWithRequestedClientAuth() {
		return Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(
								SOCKS_SERVER_PORT_USING_SSL_WITH_REQUESTED_CLIENT_AUTH)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslSettingSpecConstants.SSL_TRUST_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_FILE.getFile()),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslSettingSpecConstants.SSL_WANT_CLIENT_AUTH.newSetting(
						Boolean.TRUE)));
	}
	
	private static Configuration newConfigurationUsingSslWithRequiredClientAuth() {
		return Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(
								SOCKS_SERVER_PORT_USING_SSL_WITH_REQUIRED_CLIENT_AUTH)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH.newSetting(
						Boolean.TRUE),
				SslSettingSpecConstants.SSL_TRUST_STORE_FILE.newSetting(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_FILE.getFile()),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_PASSWORD_FILE.getContentAsString())));
	}

	private static SocksClient newSocks5ClientUsingSsl() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SSL)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithDifferentRequestedClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						SOCKS_SERVER_PORT_USING_SSL_WITH_REQUESTED_CLIENT_AUTH)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithDifferentRequiredClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						SOCKS_SERVER_PORT_USING_SSL_WITH_REQUIRED_CLIENT_AUTH)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithNoRequestedClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						SOCKS_SERVER_PORT_USING_SSL_WITH_REQUESTED_CLIENT_AUTH)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithNoRequiredClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						SOCKS_SERVER_PORT_USING_SSL_WITH_REQUIRED_CLIENT_AUTH)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSslWithRequestedClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SSL_WITH_REQUESTED_CLIENT_AUTH)
				.newSocksClient(properties);
	}
	
	private static SocksClient newSocks5ClientUsingSslWithRequiredClientAuth() {
		Properties properties = Properties.of(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_CLIENT_KEY_STORE_PASSWORD_FILE.getContentAsString()),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
						TestResourceConstants.ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SSL_WITH_REQUIRED_CLIENT_AUTH)
				.newSocksClient(properties);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		// System.setProperty("javax.net.debug", "ssl,handshake");
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
		echoServer = new EchoServer();
		echoServer.start();
		socksServerUsingSsl = new SocksServer(newConfigurationUsingSsl());
		socksServerUsingSsl.start();
		socksServerUsingSslWithRequestedClientAuth = new SocksServer(
				newConfigurationUsingSslWithRequestedClientAuth());
		socksServerUsingSslWithRequestedClientAuth.start();
		socksServerUsingSslWithRequiredClientAuth = new SocksServer(
				newConfigurationUsingSslWithRequiredClientAuth());
		socksServerUsingSslWithRequiredClientAuth.start();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		// System.clearProperty("javax.net.debug");
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}
		if (!socksServerUsingSsl.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSsl.stop();
		}
		if (!socksServerUsingSslWithRequestedClientAuth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSslWithRequestedClientAuth.stop();
		}
		if (!socksServerUsingSslWithRequiredClientAuth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSslWithRequiredClientAuth.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSsl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSsl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSsl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithDifferentRequiredClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithDifferentRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequestedClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequestedClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test(expected = IOException.class)
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithNoRequiredClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithNoRequiredClientAuth().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequestedClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequestedClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSslWithRequiredClientAuth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSslWithRequiredClientAuth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
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
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSslWithDifferentRequestedClientAuth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSslWithDifferentRequestedClientAuth().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
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
	
}
