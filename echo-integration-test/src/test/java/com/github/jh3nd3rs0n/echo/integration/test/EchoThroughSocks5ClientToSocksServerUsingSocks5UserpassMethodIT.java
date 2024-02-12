package com.github.jh3nd3rs0n.echo.integration.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;
import org.junit.rules.Timeout;

public class EchoThroughSocks5ClientToSocksServerUsingSocks5UserpassMethodIT {

	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSMETHOD = 10200;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;
	
	private static SocksServer socksServerUsingSocks5UserpassMethod;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.MINUTES)
			.withLookingForStuckThread(true)
			.build();

	private static Configuration newConfigurationUsingSocks5UserpassMethod() {
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame,");
		sb.append("Jasmine:mission%3Aimpossible,");
		sb.append("Abu:safeDriversSave40%25");
		return Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(
								SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSMETHOD)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.USERNAME_PASSWORD)),
				Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
						UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
								sb.toString()))));
	}
	
	private static SocksClient newSocks5ClientUsingSocks5UserpassMethod(
			final String username, 
			final char[] password) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.USERNAME_PASSWORD)),
				Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
						username),
				Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
						EncryptedPassword.newInstance(password)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSMETHOD))
				.newSocksClient(properties);		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
		echoServer = new EchoServer();
		echoServer.start();
		socksServerUsingSocks5UserpassMethod = new SocksServer(
				newConfigurationUsingSocks5UserpassMethod());
		socksServerUsingSocks5UserpassMethod.start();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}		
		if (!socksServerUsingSocks5UserpassMethod.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5UserpassMethod.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
}
