package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.TestServer;
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
	
	private static int socksServerPort;

	private static DatagramTestServer echoDatagramTestServer;
	private static int echoDatagramTestServerPort;
	private static TestServer echoTestServer;
	private static int echoTestServerPort;
	
	private static SocksServer socksServer;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
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
		echoDatagramTestServer = EchoDatagramTestServerHelper.newEchoDatagramTestServer(0);
		echoDatagramTestServer.start();
		echoDatagramTestServerPort = echoDatagramTestServer.getPort();
		echoTestServer = EchoTestServerHelper.newEchoTestServer(0);
		echoTestServer.start();
		echoTestServerPort = echoTestServer.getPort();		
		socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPort = socksServer.getPort().intValue();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (echoDatagramTestServer != null
				&& !echoDatagramTestServer.getState().equals(DatagramTestServer.State.STOPPED)) {
			echoDatagramTestServer.stop();
		}
		if (echoTestServer != null
				&& !echoTestServer.getState().equals(TestServer.State.STOPPED)) {
			echoTestServer.stop();
		}		
		if (socksServer != null
				&& !socksServer.getState().equals(SocksServer.State.STOPPED)) {
			socksServer.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServer01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServer02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServer03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServer04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5Client().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServer05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5Client().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServer01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServer02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServer03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5Client().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServer04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5Client().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServer05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5Client().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServer01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5Client().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServer02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5Client().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServer03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5Client().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServer04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5Client().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServer05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5Client().newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

}
