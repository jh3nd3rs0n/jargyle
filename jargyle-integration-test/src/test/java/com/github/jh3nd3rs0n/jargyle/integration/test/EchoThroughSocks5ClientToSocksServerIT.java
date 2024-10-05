package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoThroughSocks5ClientToSocksServerIT {
	
	private static int socksServerPort = 10100;

	private static DatagramEchoServer datagramEchoServer;
	private static int datagramEchoServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;
	
	private static SocksServer socksServer;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static SocksClient newSocks5Client() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPort)
				.newSocksClient(Properties.of());
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer(0);
		datagramEchoServer.start();
		datagramEchoServerPort = datagramEchoServer.getPort();
		echoServer = new EchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();		
		socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)))));
		socksServer.start();
		socksServerPort = socksServer.getPort().intValue();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (datagramEchoServer != null
				&& !datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}		
		if (socksServer != null
				&& !socksServer.getState().equals(SocksServer.State.STOPPED)) {
			socksServer.stop();
		}
		ThreadHelper.interruptableSleepForThreeSeconds();
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServer01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServer02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServer03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServer04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5Client().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5Client().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
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

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5Client().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
