package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.ResourceHelper;
import com.github.jh3nd3rs0n.jargyle.ResourceNameConstants;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;

public class SslIT {
	
	private static final int SOCKS_SERVER_PORT_USING_SSL = 9100;
	private static final int SOCKS_SERVER_PORT_USING_SSL_AND_REQUESTED_CLIENT_AUTH = 9200;
	private static final int SOCKS_SERVER_PORT_USING_SSL_AND_REQUIRED_CLIENT_AUTH = 9300;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;
	
	private static SocksServer socksServerUsingSsl;
	private static SocksServer socksServerUsingSslAndRequestedClientAuth;
	private static SocksServer socksServerUsingSslAndRequiredClientAuth;
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		// System.setProperty("javax.net.debug", "ssl,handshake");
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
		echoServer = new EchoServer();
		echoServer.start();
		socksServerUsingSsl = new SocksServer(newConfigurationUsingSsl());
		socksServerUsingSsl.start();
		socksServerUsingSslAndRequestedClientAuth = new SocksServer(
				newConfigurationUsingSslAndRequestedClientAuth());
		socksServerUsingSslAndRequestedClientAuth.start();
		socksServerUsingSslAndRequiredClientAuth = new SocksServer(
				newConfigurationUsingSslAndRequiredClientAuth());
		socksServerUsingSslAndRequiredClientAuth.start();
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
		if (!socksServerUsingSslAndRequestedClientAuth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSslAndRequestedClientAuth.stop();
		}
		if (!socksServerUsingSslAndRequiredClientAuth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSslAndRequiredClientAuth.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}
	
	private static Configuration newConfigurationUsingSsl() {
		return Configuration.newUnmodifiableInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(SOCKS_SERVER_PORT_USING_SSL)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE))));
	}

	private static Configuration newConfigurationUsingSslAndRequestedClientAuth() {
		return Configuration.newUnmodifiableInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(
								SOCKS_SERVER_PORT_USING_SSL_AND_REQUESTED_CLIENT_AUTH)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				DtlsSettingSpecConstants.DTLS_WANT_CLIENT_AUTH.newSetting(
						Boolean.TRUE),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SslSettingSpecConstants.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SslSettingSpecConstants.SSL_WANT_CLIENT_AUTH.newSetting(
						Boolean.TRUE)));
	}

	private static Configuration newConfigurationUsingSslAndRequiredClientAuth() {
		return Configuration.newUnmodifiableInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(
								SOCKS_SERVER_PORT_USING_SSL_AND_REQUIRED_CLIENT_AUTH)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				DtlsSettingSpecConstants.DTLS_NEED_CLIENT_AUTH.newSetting(
						Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH.newSetting(
						Boolean.TRUE),
				SslSettingSpecConstants.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE))));
	}
	
	private static SocksClient newSocks5ClientUsingSsl() {
		Properties properties = Properties.newInstance(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SSL)
				.newSocksClient(properties);
	}
	
	private static SocksClient newSocks5ClientUsingSslAndRequestedClientAuth() {
		Properties properties = Properties.newInstance(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SSL_AND_REQUESTED_CLIENT_AUTH)
				.newSocksClient(properties);
	}
	
	private static SocksClient newSocks5ClientUsingSslAndRequiredClientAuth() {
		Properties properties = Properties.newInstance(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SSL_AND_REQUIRED_CLIENT_AUTH)
				.newSocksClient(properties);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequestedClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndRequiredClientAuth().newSocksNetObjectFactory());
		assertEquals(string, returningString);		
	}
	
}
