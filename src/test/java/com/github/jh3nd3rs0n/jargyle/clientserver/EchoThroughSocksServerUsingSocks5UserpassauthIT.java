package com.github.jh3nd3rs0n.jargyle.clientserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.StringSourceUserRepository;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public class EchoThroughSocksServerUsingSocks5UserpassauthIT {

	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH = 10200;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;
	
	private static SocksServer socksServerUsingSocks5Userpassauth;
	
	private static Configuration newConfigurationUsingSocks5Userpassauth() {
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame ");
		sb.append("Jasmine:mission%3Aimpossible ");
		sb.append("Abu:safeDriversSave40%25");
		return Configuration.newUnmodifiableInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(
								SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
						new StringSourceUserRepository(sb.toString()))));
	}
	
	private static SocksClient newSocks5ClientUsingSocks5Userpassauth(
			final String username, 
			final char[] password) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
						username),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
						EncryptedPassword.newInstance(password)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(properties);		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
		echoServer = new EchoServer();
		echoServer.start();
		socksServerUsingSocks5Userpassauth = new SocksServer(
				newConfigurationUsingSocks5Userpassauth());
		socksServerUsingSocks5Userpassauth.start();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}		
		if (!socksServerUsingSocks5Userpassauth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5Userpassauth.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5Userpassauth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Userpassauth(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5Userpassauth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Userpassauth(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5Userpassauth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Userpassauth(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5Userpassauth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Userpassauth(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5Userpassauth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Userpassauth(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5Userpassauth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Userpassauth(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5Userpassauth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Userpassauth(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5Userpassauth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Userpassauth(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5Userpassauth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Userpassauth(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
}
