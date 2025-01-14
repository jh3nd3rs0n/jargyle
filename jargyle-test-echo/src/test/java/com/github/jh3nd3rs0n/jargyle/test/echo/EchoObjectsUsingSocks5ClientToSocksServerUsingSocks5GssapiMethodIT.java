package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
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

public class EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodIT {

	private static DatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static Server echoServer;
	private static int echoServerPort;

	private static SocksServer socksServerUsingSocks5GssapiMethod;
	private static int socksServerPortUsingSocks5GssapiMethod;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static SocksServer newSocksServerUsingSocks5GssapiMethod() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
						PositiveInteger.valueOf(500)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSocks5GssapiMethod = socksServer.getPort().intValue();
		return socksServer;
	}

	private static SocksClient newSocks5ClientUsingSocks5GssapiMethod(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_MECHANISM_OID.newPropertyWithParsedValue(
						GssEnvironment.MECHANISM_OID),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
						GssEnvironment.SERVICE_NAME),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
						protectionLevels));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSocks5GssapiMethod)
				.newSocksClient(properties);		
	}

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		GssEnvironment.setUpBeforeClass(
				EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodIT.class);
		echoDatagramServer = EchoDatagramServerHelper.newEchoDatagramServer(0);
		echoDatagramServer.start();
		echoDatagramServerPort = echoDatagramServer.getPort();
		echoServer = EchoServerHelper.newEchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		socksServerUsingSocks5GssapiMethod = newSocksServerUsingSocks5GssapiMethod();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		GssEnvironment.tearDownAfterClass(
				EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodIT.class);
		if (echoDatagramServer != null
				&& !echoDatagramServer.getState().equals(DatagramServer.State.STOPPED)) {
			echoDatagramServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(Server.State.STOPPED)) {
			echoServer.stop();
		}
		if (socksServerUsingSocks5GssapiMethod != null
				&& !socksServerUsingSocks5GssapiMethod.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethod.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod05() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_05;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection05() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_05;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(newSocks5ClientUsingSocks5GssapiMethod(
				ProtectionLevels.of(
						ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection05() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_05;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
