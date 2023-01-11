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
import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientHelper;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationHelper;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServerHelper;

public class Socks5DatagramSocketIT {

	private static final int SERVER_PORT = 10100;
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_USERPASS_AUTH = 10200;
	
	private static List<SocksServer> socksServers;
	private static List<SocksServer> socksServersUsingSocks5UserpassAuth;
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		DatagramSocketEchoHelper.startEchoServer();
		socksServers = SocksServerHelper.newStartedSocksServers(Arrays.asList(
				ConfigurationHelper.newConfiguration(SERVER_PORT)));
		socksServersUsingSocks5UserpassAuth = 
				SocksServerHelper.newStartedSocksServers(Arrays.asList(
						ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth(
								SOCKS_SERVER_PORT_USING_SOCKS5_USERPASS_AUTH)));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		DatagramSocketEchoHelper.stopEchoServer();
		SocksServerHelper.stopSocksServers(socksServers);
		SocksServerHelper.stopSocksServers(socksServersUsingSocks5UserpassAuth);
		ThreadHelper.sleepForThreeSeconds();
	}

	@Test
	public void testThroughSocks5DatagramSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5UserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5UserpassAuth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASS_AUTH, 
						"Aladdin",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5UserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5UserpassAuth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASS_AUTH, 
						"Jasmine",
						"mission:impossible".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5UserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5UserpassAuth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASS_AUTH, 
						"Abu",
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

}
