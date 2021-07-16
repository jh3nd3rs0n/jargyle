package jargyle.net.socks.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.net.DatagramSocketHelper;
import jargyle.net.Port;
import jargyle.net.ServerSocketHelper;
import jargyle.net.SocketHelper;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.server.Configuration;
import jargyle.net.socks.server.ImmutableConfiguration;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;
import jargyle.net.socks.server.v5.userpassauth.StringSourceUsernamePasswordAuthenticator;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.security.EncryptedPassword;

public class ChainingIT {

	private static List<Configuration> newChainedConfigurations() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(23456)),
						SettingSpec.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(65432))))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(65432)),
						SettingSpec.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(34567))))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(34567)))));
	}
	
	private static List<Configuration> newChainedConfigurationsEachUsingUserpassAuth() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(23456)),
						SettingSpec.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
								new StringSourceUsernamePasswordAuthenticator(
										"Aladdin:opensesame")),
						SettingSpec.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(65432))),
						SettingSpec.CHAINING_SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.CHAINING_SOCKS5_USERPASSAUTH_USERNAME_PASSWORD.newSetting(
								UsernamePassword.newInstance("Jasmine:mission%3Aimpossible")))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(65432)),
						SettingSpec.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
								new StringSourceUsernamePasswordAuthenticator(
										"Jasmine:mission%3Aimpossible")),
						SettingSpec.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(34567))),
						SettingSpec.CHAINING_SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.CHAINING_SOCKS5_USERPASSAUTH_USERNAME_PASSWORD.newSetting(
								UsernamePassword.newInstance("Abu:safeDriversSave40%25")))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(34567)),
						SettingSpec.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
								new StringSourceUsernamePasswordAuthenticator(
										"Abu:safeDriversSave40%25")))));
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurations() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(23456))
				.newSocksClient(Properties.newInstance());
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(65432))
				.newSocksClient(Properties.newInstance(), client1);
		SocksClient client3 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(34567))
				.newSocksClient(Properties.newInstance(), client2);
		return client3;
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(23456))
				.newSocksClient(Properties.newInstance(
						PropertySpec.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						PropertySpec.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Aladdin"),
						PropertySpec.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"opensesame".toCharArray()))));
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(65432))
				.newSocksClient(Properties.newInstance(
						PropertySpec.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						PropertySpec.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Jasmine"),
						PropertySpec.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"mission:impossible".toCharArray()))), 
						client1);
		SocksClient client3 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(34567))
				.newSocksClient(Properties.newInstance(
						PropertySpec.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						PropertySpec.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Abu"),
						PropertySpec.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"safeDriversSave40%".toCharArray()))), 
						client2);
		return client3;
	}
	
	private static List<Configuration> newConfigurations() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(23456)))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(65432)))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(34567)))));
	}
	
	private static List<Configuration> newConfigurationsEachUsingUserpassAuth() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(23456)),
						SettingSpec.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
								new StringSourceUsernamePasswordAuthenticator(
										"Aladdin:opensesame")))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(65432)),
						SettingSpec.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
								new StringSourceUsernamePasswordAuthenticator(
										"Jasmine:mission%3Aimpossible")))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						SettingSpec.PORT.newSetting(Port.newInstance(34567)),
						SettingSpec.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
								new StringSourceUsernamePasswordAuthenticator(
										"Abu:safeDriversSave40%25")))));
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurations() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(23456))
				.newSocksClient(Properties.newInstance());
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(23456))
				.newSocksClient(Properties.newInstance(
						PropertySpec.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						PropertySpec.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Aladdin"),
						PropertySpec.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"opensesame".toCharArray()))));
	}
	
	// Socks5DatagramSocket
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}

	// Socks5ServerSocket
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
	// Socks5Socket
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurations(), 
				newConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsEachUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsEachUsingUserpassAuth(), 
				newConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurations(), 
				newChainedConfigurations());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsEachUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsEachUsingUserpassAuth(), 
				newChainedConfigurationsEachUsingUserpassAuth());
		assertEquals(string, returningString);
	}
	
}
