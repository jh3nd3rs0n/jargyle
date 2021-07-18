package jargyle.net.socks.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
import jargyle.TestStringConstants;
import jargyle.net.DatagramSocketHelper;
import jargyle.net.ServerSocketHelper;
import jargyle.net.SocketHelper;
import jargyle.net.socks.client.v5.Socks5ServerUri;
import jargyle.net.socks.server.Configuration;
import jargyle.net.socks.server.ImmutableConfiguration;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;

public class SslIT {

/*	
	@org.junit.BeforeClass
	public static void setUp() {
		System.setProperty("javax.net.debug", "ssl,handshake");
	}
	
	@org.junit.AfterClass
	public static void tearDown() {
		System.clearProperty("javax.net.debug");
	}
*/
	
	private static Configuration newConfigurationUsingSsl() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.DTLS_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE))));
	}

	private static Configuration newConfigurationUsingSslAndRequestedClientAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.DTLS_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.DTLS_WANT_CLIENT_AUTH.newSetting(Boolean.TRUE),				
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_WANT_CLIENT_AUTH.newSetting(Boolean.TRUE)));
	}

	private static Configuration newConfigurationUsingSslAndRequiredClientAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.DTLS_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.DTLS_NEED_CLIENT_AUTH.newSetting(Boolean.TRUE),
				SettingSpec.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),				
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_NEED_CLIENT_AUTH.newSetting(Boolean.TRUE),
				SettingSpec.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				SettingSpec.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE))));
	}
	
	private static SocksClient newSocks5ClientUsingSsl(
			final String host, 
			final Integer port) {
		Properties properties = Properties.newInstance(
				PropertySpec.DTLS_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				PropertySpec.SSL_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	private static SocksClient newSocks5ClientUsingSslAndClientAuth(
			final String host, 
			final Integer port) {
		Properties properties = Properties.newInstance(
				PropertySpec.DTLS_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.DTLS_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				PropertySpec.DTLS_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				PropertySpec.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				PropertySpec.SSL_ENABLED.newProperty(Boolean.TRUE),
				PropertySpec.SSL_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_FILE)),
				PropertySpec.SSL_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_FILE)),
				PropertySpec.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}
	
}
