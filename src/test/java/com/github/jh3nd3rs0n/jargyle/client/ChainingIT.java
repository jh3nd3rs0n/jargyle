package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.ResourceHelper;
import com.github.jh3nd3rs0n.jargyle.ResourceNameConstants;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.ChainingDtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingGeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSocks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServerHelper;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUserRepository;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public class ChainingIT {
	
	private static final int CHAINED_SOCKS_SERVER_PORT_1 = 2100;
	private static final int CHAINED_SOCKS_SERVER_PORT_2 = 2200;
	private static final int CHAINED_SOCKS_SERVER_PORT_3 = 2300;
	
	private static final int CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH = 3100;
	private static final int CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH = 3200;
	private static final int CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH = 3300;
	
	private static final int CHAINED_SOCKS_SERVER_PORT_1_USING_SSL = 4100;
	private static final int CHAINED_SOCKS_SERVER_PORT_2_USING_SSL = 4200;

	private static final int SOCKS_SERVER_PORT_1 = 5100;
	private static final int SOCKS_SERVER_PORT_2 = 5200;
	private static final int SOCKS_SERVER_PORT_3 = 5300;
	
	private static final int SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH = 6100;
	private static final int SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH = 6200;
	private static final int SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH = 6300;
	
	private static final int SOCKS_SERVER_PORT_1_USING_SSL = 7100;
	private static final int SOCKS_SERVER_PORT_2_USING_SSL = 7200;

	private static List<SocksServer> chainedSocksServers;
	private static List<SocksServer> chainedSocksServersUsingSocks5Userpassauth;
	private static List<SocksServer> chainedSocksServersUsingSsl;
	
	private static List<SocksServer> socksServers;
	private static List<SocksServer> socksServersUsingSocks5Userpassauth;
	private static List<SocksServer> socksServersUsingSsl;
	
	private static List<Configuration> newChainedConfigurations() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_1)),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_2))))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_2)),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_3))))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_3)))));
	}
	
	private static List<Configuration> newChainedConfigurationsUsingSocks5Userpassauth() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Aladdin:opensesame")),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH))),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_USERNAME.newSetting(
								"Jasmine"),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_PASSWORD.newSetting(
								EncryptedPassword.newInstance("mission:impossible".toCharArray())))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Jasmine:mission%3Aimpossible")),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH))),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_USERNAME.newSetting(
								"Abu"),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_PASSWORD.newSetting(
								EncryptedPassword.newInstance("safeDriversSave40%".toCharArray())))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Abu:safeDriversSave40%25")))));
	}
	
	private static List<Configuration> newChainedConfigurationsUsingSsl() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_1_USING_SSL)),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_2_USING_SSL))),
						ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED.newSetting(Boolean.TRUE),
						ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_FILE.newSetting(
								ResourceHelper.getResourceAsFile(
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
						ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
								ResourceHelper.getResourceAsString(
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
						ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED.newSetting(Boolean.TRUE),
						ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_FILE.newSetting(
								ResourceHelper.getResourceAsFile(
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
						ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
								ResourceHelper.getResourceAsString(
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_2_USING_SSL)),
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
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)))));		
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurations() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_1))
				.newSocksClient(Properties.newInstance());
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_2))
				.newSocksClient(Properties.newInstance(), client1);
		SocksClient client3 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_3))
				.newSocksClient(Properties.newInstance(), client2);
		return client3;
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Aladdin"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"opensesame".toCharArray()))));
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Jasmine"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"mission:impossible".toCharArray()))), 
						client1);
		SocksClient client3 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Abu"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"safeDriversSave40%".toCharArray()))), 
						client2);
		return client3;
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurationsUsingSsl() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_1_USING_SSL))
				.newSocksClient(Properties.newInstance());
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_2_USING_SSL))
				.newSocksClient(Properties.newInstance(
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
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE))), 
						client1);
		return client2;
	}
	
	private static List<Configuration> newConfigurations() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_1)))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_2)))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_3)))));
	}
	
	private static List<Configuration> newConfigurationsUsingSocks5Userpassauth() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Aladdin:opensesame")))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Jasmine:mission%3Aimpossible")))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Abu:safeDriversSave40%25")))));
	}
	
	private static List<Configuration> newConfigurationsUsingSsl() {
		return Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_1_USING_SSL)))),
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_2_USING_SSL)),
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
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)))));
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurations() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_1))
				.newSocksClient(Properties.newInstance());
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Aladdin"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"opensesame".toCharArray()))));
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurationsUsingSsl() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_1_USING_SSL))
				.newSocksClient(Properties.newInstance());
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		DatagramSocketEchoHelper.startEchoServer();
		SocketEchoHelper.startEchoServer();
		chainedSocksServers = SocksServerHelper.newStartedSocksServers(
				newChainedConfigurations());
		chainedSocksServersUsingSocks5Userpassauth = 
				SocksServerHelper.newStartedSocksServers(
						newChainedConfigurationsUsingSocks5Userpassauth());
		chainedSocksServersUsingSsl = SocksServerHelper.newStartedSocksServers(
				newChainedConfigurationsUsingSsl());
		socksServers = SocksServerHelper.newStartedSocksServers(
				newConfigurations());
		socksServersUsingSocks5Userpassauth = 
				SocksServerHelper.newStartedSocksServers(
						newConfigurationsUsingSocks5Userpassauth());
		socksServersUsingSsl = SocksServerHelper.newStartedSocksServers(
				newConfigurationsUsingSsl());		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		DatagramSocketEchoHelper.stopEchoServer();
		SocketEchoHelper.stopEchoServer();
		SocksServerHelper.stopSocksServers(chainedSocksServers);
		SocksServerHelper.stopSocksServers(chainedSocksServersUsingSocks5Userpassauth);
		SocksServerHelper.stopSocksServers(chainedSocksServersUsingSsl);
		SocksServerHelper.stopSocksServers(socksServers);
		SocksServerHelper.stopSocksServers(socksServersUsingSocks5Userpassauth);
		SocksServerHelper.stopSocksServers(socksServersUsingSsl);
		ThreadHelper.sleepForThreeSeconds();
	}
	
	// Socks5DatagramSocket
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingChainedSocks5ClientToConfigurationsUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5ClientToChainedConfigurationsUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	// Socks5ServerSocket
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingChainedSocks5ClientToConfigurationsUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5ClientToChainedConfigurationsUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	// Socks5Socket
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingChainedSocks5ClientToConfigurationsUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurations01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurations02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurations03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5ClientToChainedConfigurationsUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
}
