package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
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

public class EchoThroughSocks5ClientToSocksServerUsingSocks5UserpassMethodIT {

	private static DatagramTestServer echoDatagramTestServer;
	private static int echoDatagramTestServerPort;
	private static TestServer echoTestServer;
	private static int echoTestServerPort;
	
	private static SocksServer socksServerUsingSocks5UserpassMethod;
	private static int socksServerPortUsingSocks5UserpassMethod;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static SocksServer newSocksServerUsingSocks5UserpassMethod() throws IOException {
        String s = "Aladdin:opensesame," +
                "Jasmine:mission%3Aimpossible," +
                "Abu:safeDriversSave40%25," +
                "Jafar:opensesame";
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.USERNAME_PASSWORD)),
				Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
						UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
								s)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(DatagramTestServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSocks5UserpassMethod = socksServer.getPort().intValue();
		return socksServer;
	}
	
	private static SocksClient newSocks5ClientUsingSocks5UserpassMethod(
			final String username, 
			final char[] password) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.USERNAME_PASSWORD)),
				Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
						username),
				Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
						EncryptedPassword.newInstance(password)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSocks5UserpassMethod)
				.newSocksClient(properties);		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		echoDatagramTestServer = EchoDatagramTestServerHelper.newEchoDatagramTestServer(0);
		echoDatagramTestServer.start();
		echoDatagramTestServerPort = echoDatagramTestServer.getPort();
		echoTestServer = EchoTestServerHelper.newEchoTestServer(0);
		echoTestServer.start();
		echoTestServerPort = echoTestServer.getPort();
		socksServerUsingSocks5UserpassMethod =
				newSocksServerUsingSocks5UserpassMethod();
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
		if (socksServerUsingSocks5UserpassMethod != null
				&& !socksServerUsingSocks5UserpassMethod.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5UserpassMethod.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoDatagramTestClient echoDatagramTestClient = new EchoDatagramTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoDatagramTestClient.echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		TestServer echTestServer = EchoTestServerHelper.newEchoTestServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_05;
		String returningString = EchoTestServerHelper.startThenEchoThenStop(
				echTestServer, new EchoTestClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoTestClient echoTestClient = new EchoTestClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_05;
		String returningString = echoTestClient.echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

}
