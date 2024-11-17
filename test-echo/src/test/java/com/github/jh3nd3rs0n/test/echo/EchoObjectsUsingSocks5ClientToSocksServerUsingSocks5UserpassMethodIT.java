package com.github.jh3nd3rs0n.test.echo;

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
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5UserpassMethodIT {

	private static DatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static Server echoServer;
	private static int echoServerPort;
	
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
						PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
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

	private static SocksClient newSocks5ClientUsingNoSocks5UserpassMethod(
			final String username,
			final char[] password) {
		String usrname;
        try {
            usrname = URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
		String psswrd;
        try {
            psswrd = URLEncoder.encode(new String(password), "UTF-8");
        } catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
        }
        return Scheme.SOCKS5.newSocksServerUri(
				usrname + ":" + psswrd,
				InetAddress.getLoopbackAddress().getHostAddress(),
				socksServerPortUsingSocks5UserpassMethod)
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
		socksServerUsingSocks5UserpassMethod =
				newSocksServerUsingSocks5UserpassMethod();
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
		if (socksServerUsingSocks5UserpassMethod != null
				&& !socksServerUsingSocks5UserpassMethod.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5UserpassMethod.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory(), 0); 
		String string = StringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_05;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory()); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Aladdin",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jasmine",
						"mission:impossible".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Abu",
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Aladdin",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jasmine",
						"mission:impossible".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Abu",
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		Server echServer = EchoServerHelper.newEchoServer(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory(), 0);
		String string = StringConstants.STRING_05;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Aladdin",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jasmine",
						"mission:impossible".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Abu",
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientUsingNoSocks5UserpassMethodToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingNoSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
}
