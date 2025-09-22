package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
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

public class EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodIT {

	private static EchoDatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;

	private static AbstractSocksServer socksServerUsingSocks5GssapiMethod;
	private static int socksServerPortUsingSocks5GssapiMethod;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static AbstractSocksServer newSocksServerUsingSocks5GssapiMethod() throws IOException {
		AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
						PositiveInteger.valueOf(500)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSocks5GssapiMethod = socksServer.getPort();
		return socksServer;
	}

	private static NetObjectFactory newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
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
				.newSocksClient(properties)
                .newSocksNetObjectFactory();		
	}

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		GssEnvironment.setUpBeforeClass(
				EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodIT.class);
		echoDatagramServer = EchoDatagramServer.newInstance(0);
		echoDatagramServer.start();
		echoDatagramServerPort = echoDatagramServer.getPort();
		echoServer = EchoServer.newInstance(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		socksServerUsingSocks5GssapiMethod = newSocksServerUsingSocks5GssapiMethod();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		GssEnvironment.tearDownAfterClass(
				EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodIT.class);
		if (echoDatagramServer != null
				&& !echoDatagramServer.getState().equals(EchoDatagramServer.State.STOPPED)) {
			echoDatagramServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}
		if (socksServerUsingSocks5GssapiMethod != null
				&& !socksServerUsingSocks5GssapiMethod.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethod.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethod05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0); 
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0); 
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0); 
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0); 
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
				ProtectionLevels.of(
						ProtectionLevel.REQUIRED_INTEG)), 0); 
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0); 
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithIntegProtection05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
