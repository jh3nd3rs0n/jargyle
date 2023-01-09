package com.github.jh3nd3rs0n.jargyle.client.socks5;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.SocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientHelper;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationHelper;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServerHelper;

public class Socks5SocketIT {

	private static final int SERVER_PORT = 30100;
	private static final int SERVER_PORT_USING_SOCKS5_USERPASS_AUTH = 30200;
	
	private static List<SocksServer> socksServers;
	private static List<SocksServer> socksServersUsingSocks5UserpassAuth;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		SocketEchoHelper.startEchoServer();
		socksServers = SocksServerHelper.newStartedSocksServers(Arrays.asList(
				ConfigurationHelper.newConfiguration(SERVER_PORT)));
		socksServersUsingSocks5UserpassAuth = 
				SocksServerHelper.newStartedSocksServers(Arrays.asList(
						ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth(
								SERVER_PORT_USING_SOCKS5_USERPASS_AUTH)));		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		SocketEchoHelper.stopEchoServer();
		SocksServerHelper.stopSocksServers(socksServers);
		SocksServerHelper.stopSocksServers(socksServersUsingSocks5UserpassAuth);
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testThroughSocks5Socket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5Socket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5Socket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5UserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5UserpassAuth(
						SERVER_PORT_USING_SOCKS5_USERPASS_AUTH, 
						"Aladdin",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5UserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5UserpassAuth(
						SERVER_PORT_USING_SOCKS5_USERPASS_AUTH, 
						"Jasmine",
						"mission:impossible".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5UserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5UserpassAuth(
						SERVER_PORT_USING_SOCKS5_USERPASS_AUTH, 
						"Abu",
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
}
