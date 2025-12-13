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

public class EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSslIT {

	private static EchoDatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;
	
	private static AbstractSocksServer socksServerUsingSsl;
	private static int socksServerPortUsingSsl;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static AbstractSocksServer newSocksServerUsingSsl() throws IOException {
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
				SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
						PositiveInteger.valueOf(500)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSsl = socksServer.getPort();
		return socksServer;
	}

	private static NetObjectFactory newSocks5NetObjectFactoryUsingSsl() {
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
						socksServerPortUsingSsl)
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
		socksServerUsingSsl = newSocksServerUsingSsl();
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
		if (socksServerUsingSsl != null
				&& !socksServerUsingSsl.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
			socksServerUsingSsl.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSsl()); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSsl()); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSsl()); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSsl());
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSsl());
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSsl()); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSsl()); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSsl()); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);		
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSsl());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSsl05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSsl());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSsl01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSsl(), 0); 
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSsl02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSsl(), 0); 
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSsl03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSsl(), 0); 
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSsl04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSsl(), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSsl05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSsl(), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
