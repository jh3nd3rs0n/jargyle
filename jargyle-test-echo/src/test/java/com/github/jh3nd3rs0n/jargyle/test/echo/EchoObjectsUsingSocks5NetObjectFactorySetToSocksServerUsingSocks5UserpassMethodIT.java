package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
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

public class EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodIT {

	private static EchoDatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;
	
	private static AbstractSocksServer socksServerUsingSocks5UserpassMethod;
	private static int socksServerPortUsingSocks5UserpassMethod;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static AbstractSocksServer newSocksServerUsingSocks5UserpassMethod() throws IOException {
        String s = "Aladdin:opensesame," +
                "Jasmine:mission%3Aimpossible," +
                "Abu:safeDriversSave40%25," +
                "Jafar:opensesame";
		AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.USERNAME_PASSWORD)),
				Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
						UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
								s)),
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
						PositiveInteger.valueOf(500)),
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
						PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
		socksServer.start();
		socksServerPortUsingSocks5UserpassMethod = socksServer.getPort();
		return socksServer;
	}
	
	private static NetObjectFactory newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
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
				.newSocksClient(properties)
                .newSocksNetObjectFactory();		
	}

	private static NetObjectFactory newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
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
				.newSocksClient(Properties.of())
                .newSocksNetObjectFactory();
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		echoDatagramServer = EchoDatagramServer.newInstance(0);
		echoDatagramServer.start();
		echoDatagramServerPort = echoDatagramServer.getPort();
		echoServer = EchoServer.newInstance(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		socksServerUsingSocks5UserpassMethod =
				newSocksServerUsingSocks5UserpassMethod();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (echoDatagramServer != null
				&& !echoDatagramServer.getState().equals(EchoDatagramServer.State.STOPPED)) {
			echoDatagramServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}		
		if (socksServerUsingSocks5UserpassMethod != null
				&& !socksServerUsingSocks5UserpassMethod.getState().equals(
                        AbstractSocksServer.State.STOPPED)) {
			socksServerUsingSocks5UserpassMethod.stop();
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
                        "Bogus",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
                        "Aladdin",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray())); 
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray())); 
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray())); 
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
                        "Bogus",
                        "12345678".toCharArray()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
                        "Aladdin",
                        "12345678".toCharArray()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray()), 0); 
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray()), 0); 
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray()), 0); 
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
                        "Bogus",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
                        "Aladdin",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Aladdin", 
						"opensesame".toCharArray())); 
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jasmine", 
						"mission:impossible".toCharArray())); 
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Abu", 
						"safeDriversSave40%".toCharArray())); 
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethod05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethod(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitlyForIOException01() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
                        "Bogus",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_01;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitlyForIOException02() throws IOException {
        EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
                        "Aladdin",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_02;
        String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
        assertEquals(string, returningString);
    }

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly01() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Aladdin",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_01;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly02() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jasmine",
						"mission:impossible".toCharArray()));
		String string = StringConstants.STRING_02;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly03() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Abu",
						"safeDriversSave40%".toCharArray()));
		String string = StringConstants.STRING_03;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly04() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_04;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly05() throws IOException {
		EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_05;
		String returningString = echoDatagramClient.echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitlyForIOException01() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
                        "Bogus",
                        "12345678".toCharArray()), 0);
        String string = StringConstants.STRING_01;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitlyForIOException02() throws IOException {
        EchoServer echServer = EchoServer.newInstance(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
                        "Aladdin",
                        "12345678".toCharArray()), 0);
        String string = StringConstants.STRING_02;
        String returningString = echServer.startThenEchoThenStop(
                new EchoClient(), string);
        assertEquals(string, returningString);
    }

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly01() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Aladdin",
						"opensesame".toCharArray()), 0);
		String string = StringConstants.STRING_01;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly02() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jasmine",
						"mission:impossible".toCharArray()), 0);
		String string = StringConstants.STRING_02;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly03() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Abu",
						"safeDriversSave40%".toCharArray()), 0);
		String string = StringConstants.STRING_03;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly04() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jafar",
						"opensesame".toCharArray()), 0);
		String string = StringConstants.STRING_04;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly05() throws IOException {
		EchoServer echServer = EchoServer.newInstance(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jafar",
						"opensesame".toCharArray()), 0);
		String string = StringConstants.STRING_05;
		String returningString = echServer.startThenEchoThenStop(
				new EchoClient(), string);
		assertEquals(string, returningString);
	}

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitlyForIOException01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
                        "Bogus",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitlyForIOException02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
                        "Aladdin",
                        "12345678".toCharArray()));
        String string = StringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Aladdin",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jasmine",
						"mission:impossible".toCharArray()));
		String string = StringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Abu",
						"safeDriversSave40%".toCharArray()));
		String string = StringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5UserpassMethodImplicitly05() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5NetObjectFactoryUsingSocks5UserpassMethodImplicitly(
						"Jafar",
						"opensesame".toCharArray()));
		String string = StringConstants.STRING_05;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

}
