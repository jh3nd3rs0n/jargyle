package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.*;
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

public class EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT {

	private static EchoDatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;

	private static AbstractSocksServer socksServerUsingSocks5GssapiAuthMethod;
	private static int socksServerPortUsingSocks5GssapiAuthMethod;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static AbstractSocksServer newSocksServerUsingSocks5GssapiAuthMethod() throws IOException {
		AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)),
				SocksSettingSpecConstants.SOCKS_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
						PositiveInteger.valueOf(500)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSocks5GssapiAuthMethod = socksServer.getPort();
		return socksServer;
	}

	private static NetObjectFactory newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID.newPropertyWithParsedValue(
						GssEnvironment.MECHANISM_OID),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME.newProperty(
						GssEnvironment.SERVICE_NAME),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.newProperty(
						protectionLevels));
		return SocksServerUriScheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSocks5GssapiAuthMethod)
				.newSocksClient(properties)
                .newSocksNetObjectFactory();		
	}

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		GssEnvironment.setUpBeforeClass(
				EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
		echoDatagramServer = EchoDatagramServer.newInstance(0);
		echoDatagramServer.start();
		echoDatagramServerPort = echoDatagramServer.getPort();
		echoServer = EchoServer.newInstance(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		socksServerUsingSocks5GssapiAuthMethod = newSocksServerUsingSocks5GssapiAuthMethod();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		GssEnvironment.tearDownAfterClass(
				EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
		if (echoDatagramServer != null
				&& !echoDatagramServer.getState().equals(EchoDatagramServer.State.STOPPED)) {
			echoDatagramServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}
		if (socksServerUsingSocks5GssapiAuthMethod != null
				&& !socksServerUsingSocks5GssapiAuthMethod.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiAuthMethod.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF))); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG))); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethod05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0); 
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0); 
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0); 
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegAndConfProtection05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0); 
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
				ProtectionLevels.of(
						ProtectionLevel.REQUIRED_INTEG)), 0); 
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0); 
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithIntegProtection05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5GssapiAuthMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
