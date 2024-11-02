package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.test.help.net.Server;
import com.github.jh3nd3rs0n.test.help.string.StringConstants;
import com.github.jh3nd3rs0n.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoObjectsUsingSocks5ClientToSocksServerTest {
	
	private static int nettySocksServerPort;
	private static int socksServerPort;

	private static DatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static Server echoServer;
	private static int echoServerPort;

	private static com.github.jh3nd3rs0n.test.netty.example.socksproxy.SocksServer nettySocksServer;
	private static SocksServer socksServer;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static SocksClient newSocks5ClientToNettySocksServer() {
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						nettySocksServerPort)
				.newSocksClient(Properties.of());
	}

	private static SocksClient newSocks5ClientToSocksServer() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPort)
				.newSocksClient(Properties.of());
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		echoDatagramServer = EchoDatagramServerHelper.newEchoDatagramServer(0);
		echoDatagramServer.start();
		echoDatagramServerPort = echoDatagramServer.getPort();
		echoServer = EchoServerHelper.newEchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		nettySocksServer = new com.github.jh3nd3rs0n.test.netty.example.socksproxy.SocksServer();
		nettySocksServer.start();
		nettySocksServerPort = nettySocksServer.getPort();
		socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPort = socksServer.getPort().intValue();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (echoDatagramServer != null
				&& !echoDatagramServer.getState().equals(DatagramServer.State.STOPPED)) {
			echoDatagramServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(Server.State.STOPPED)) {
			echoServer.stop();
		}
		if (nettySocksServer != null 
				&& !nettySocksServer.getState().equals(
						com.github.jh3nd3rs0n.test.netty.example.socksproxy.SocksServer.State.STOPPED)) {
			nettySocksServer.stop();
		}
		if (socksServer != null
				&& !socksServer.getState().equals(SocksServer.State.STOPPED)) {
			socksServer.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServer01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServer02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServer03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServer04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServer05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToNettySocksServer01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToNettySocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToNettySocksServer02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToNettySocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToNettySocksServer03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToNettySocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToNettySocksServer04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToNettySocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToNettySocksServer05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToNettySocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServer05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer01() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer02() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer03() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer04() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServer05() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientToSocksServer().newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_05;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
