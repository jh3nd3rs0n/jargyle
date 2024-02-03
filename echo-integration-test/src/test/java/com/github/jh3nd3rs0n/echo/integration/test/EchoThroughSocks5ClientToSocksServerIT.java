package com.github.jh3nd3rs0n.echo.integration.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;

public class EchoThroughSocks5ClientToSocksServerIT {
	
	private static final int SOCKS_SERVER_PORT = 10100;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;
	
	private static SocksServer socksServer;
	
	private static Configuration newConfiguration() {
		return Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(SOCKS_SERVER_PORT))));
	}
	
	private static SocksClient newSocks5Client() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT))
				.newSocksClient(Properties.of());
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
		echoServer = new EchoServer();
		echoServer.start();		
		socksServer = new SocksServer(newConfiguration());
		socksServer.start();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}		
		if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
			socksServer.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServer01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServer02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServer03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5Client().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5Client().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5Client().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
}
