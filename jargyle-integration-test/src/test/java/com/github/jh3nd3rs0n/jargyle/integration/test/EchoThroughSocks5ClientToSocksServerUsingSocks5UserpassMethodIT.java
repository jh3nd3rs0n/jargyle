package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
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

	private static DatagramEchoServer datagramEchoServer;
	private static int datagramEchoServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;
	
	private static SocksServer socksServerUsingSocks5UserpassMethod;
	private static int socksServerPortUsingSocks5Userpassmethod;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.SECONDS)
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
								s)))));
		socksServer.start();
		socksServerPortUsingSocks5Userpassmethod = socksServer.getPort().intValue();
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
						socksServerPortUsingSocks5Userpassmethod)
				.newSocksClient(properties);		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer(0);
		datagramEchoServer.start();
		datagramEchoServerPort = datagramEchoServer.getPort();
		echoServer = new EchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		socksServerUsingSocks5UserpassMethod =
				newSocksServerUsingSocks5UserpassMethod();
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
		if (socksServerUsingSocks5UserpassMethod != null
				&& !socksServerUsingSocks5UserpassMethod.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5UserpassMethod.stop();
		}
		ThreadHelper.interruptableSleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

}
