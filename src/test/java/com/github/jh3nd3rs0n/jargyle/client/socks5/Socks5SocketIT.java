package com.github.jh3nd3rs0n.jargyle.client.socks5;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.EchoClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientHelper;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationHelper;
import com.github.jh3nd3rs0n.jargyle.server.EchoServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

public class Socks5SocketIT {

	private static final int SOCKS_SERVER_PORT = 30100;
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH = 30200;
	
	private static EchoServer echoServer;
	
	private static SocksServer socksServer;
	private static SocksServer socksServerUsingSocks5Userpassauth;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		echoServer = new EchoServer();
		echoServer.start();
		socksServer = new SocksServer(
				ConfigurationHelper.newConfiguration(SOCKS_SERVER_PORT));
		socksServer.start();
		socksServerUsingSocks5Userpassauth = new SocksServer(
				ConfigurationHelper.newConfigurationUsingSocks5Userpassauth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH));
		socksServerUsingSocks5Userpassauth.start();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}
		if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
			socksServer.stop();
		}
		if (!socksServerUsingSocks5Userpassauth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5Userpassauth.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testThroughSocks5Socket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewSocket(
				string, 
				SocksClientHelper.newSocks5Client(SOCKS_SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5Socket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewSocket(
				string, 
				SocksClientHelper.newSocks5Client(SOCKS_SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5Socket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewSocket(
				string, 
				SocksClientHelper.newSocks5Client(SOCKS_SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echoThroughNewSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5Userpassauth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH, 
						"Aladdin",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echoThroughNewSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5Userpassauth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH, 
						"Jasmine",
						"mission:impossible".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echoThroughNewSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5Userpassauth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH, 
						"Abu",
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
}
