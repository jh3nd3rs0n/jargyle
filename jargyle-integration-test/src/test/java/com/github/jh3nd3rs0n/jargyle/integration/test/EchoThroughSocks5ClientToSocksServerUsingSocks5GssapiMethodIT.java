package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.test.help.gss.kerberos.TestKerberosEnvironment;
import com.github.jh3nd3rs0n.jargyle.test.help.gss.kerberos.TestKerberosEnvironmentException;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoThroughSocks5ClientToSocksServerUsingSocks5GssapiMethodIT {

	private static final String BASE_DIR_PREFIX =
			"com.github.jh3nd3rs0n.jargyle.integration.test-";

	private static final String KDC_REALM = "EXAMPLE.COM";
	private static final String KDC_HOST = 
			InetAddress.getLoopbackAddress().getHostAddress();
	private static final int KDC_PORT = 8000;

	private static final String PRINCIPAL = "alice";
	private static final String PRINCIPAL_PASSWORD = "87654321";

	private static final String RCMD_SERVICE_PRINCIPAL = String.format(
			"rcmd/%s", InetAddress.getLoopbackAddress().getHostAddress());
	private static final String RCMD_SERVICE_PRINCIPAL_PASSWORD = "12345678";

	private static Path baseDir = null;

	private static TestKerberosEnvironment testKerberosEnvironment;

	private static DatagramEchoServer datagramEchoServer;
	private static int datagramEchoServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;

	private static SocksServer socksServerUsingSocks5GssapiMethod;
	private static int socksServerPortUsingSocks5Gssapimethod;
	private static SocksServer socksServerUsingSocks5GssapiMethodNecReferenceImpl;
	private static int socksServerPortUsingSocks5GssapimethodNecReferenceImpl;
	private static SocksServer socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection;
	private static int socksServerPortUsingSocks5GssapimethodWithSelectiveIntegOrConfProtection;
	private static SocksServer socksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection;
	private static int socksServerPortUsingSocks5GssapimethodNecReferenceImplWithSelectiveIntegOrConfProtection;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	private static SocksServer newSocksServerUsingSocks5GssapiMethod() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)))));
		socksServer.start();
		socksServerPortUsingSocks5Gssapimethod = socksServer.getPort().intValue();
		return socksServer;
	}

	private static SocksServer newSocksServerUsingSocks5GssapiMethodNecReferenceImpl() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE))));
		socksServer.start();
		socksServerPortUsingSocks5GssapimethodNecReferenceImpl = socksServer.getPort().intValue();
		return socksServer;
	}

	private static SocksServer newSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newSetting(
						ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG.newSetting(
						1),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF.newSetting(
						Boolean.TRUE))));
		socksServer.start();
		socksServerPortUsingSocks5GssapimethodWithSelectiveIntegOrConfProtection =
				socksServer.getPort().intValue();
		return socksServer;
	}

	private static SocksServer newSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection() throws IOException {
		SocksServer socksServer = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
						Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(0)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newSetting(
						ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG.newSetting(
						1),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF.newSetting(
						Boolean.TRUE))));
		socksServer.start();
		socksServerPortUsingSocks5GssapimethodNecReferenceImplWithSelectiveIntegOrConfProtection =
				socksServer.getPort().intValue();
		return socksServer;
	}

	private static SocksClient newSocks5ClientUsingSocks5GssapiMethod(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
						protectionLevels));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSocks5Gssapimethod)
				.newSocksClient(properties);		
	}
	
	private static SocksClient newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.newProperty(
						Boolean.TRUE),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
						protectionLevels));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSocks5GssapimethodNecReferenceImpl)
				.newSocksClient(properties);		
	}

	private static SocksClient newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection() {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
						ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG.newProperty(
						1),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF.newProperty(
						Boolean.TRUE));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSocks5GssapimethodWithSelectiveIntegOrConfProtection)
				.newSocksClient(properties);
	}

	private static SocksClient newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection() {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.newProperty(
						Boolean.TRUE),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
						ProtectionLevels.of(ProtectionLevel.SELECTIVE_INTEG_OR_CONF)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG.newProperty(
						1),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF.newProperty(
						Boolean.TRUE));
		return Scheme.SOCKS5.newSocksServerUri(
						InetAddress.getLoopbackAddress().getHostAddress(),
						socksServerPortUsingSocks5GssapimethodNecReferenceImplWithSelectiveIntegOrConfProtection)
				.newSocksClient(properties);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws IOException, TestKerberosEnvironmentException {
		
		baseDir = Files.createTempDirectory(BASE_DIR_PREFIX);

		testKerberosEnvironment = new TestKerberosEnvironment.Builder(
				KDC_REALM, KDC_HOST, KDC_PORT, baseDir)
				.setAcceptorPrincipal(
						RCMD_SERVICE_PRINCIPAL,
						RCMD_SERVICE_PRINCIPAL_PASSWORD)
				.setInitiatorPrincipal(
						PRINCIPAL,
						PRINCIPAL_PASSWORD)
				.build();
		testKerberosEnvironment.setUp();

		datagramEchoServer = new DatagramEchoServer(0);
		datagramEchoServer.start();
		datagramEchoServerPort = datagramEchoServer.getPort();
		echoServer = new EchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
		socksServerUsingSocks5GssapiMethod = newSocksServerUsingSocks5GssapiMethod();
		socksServerUsingSocks5GssapiMethodNecReferenceImpl =
				newSocksServerUsingSocks5GssapiMethodNecReferenceImpl();
		socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection =
				newSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection();
		socksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection =
				newSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException, TestKerberosEnvironmentException {
		if (testKerberosEnvironment != null) {
			testKerberosEnvironment.tearDown();
		}
		if (baseDir != null) {
			Files.deleteIfExists(baseDir);
		}
		if (datagramEchoServer != null
				&& !datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}
		if (socksServerUsingSocks5GssapiMethod != null
				&& !socksServerUsingSocks5GssapiMethod.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethod.stop();
		}
		if (socksServerUsingSocks5GssapiMethodNecReferenceImpl != null
				&& !socksServerUsingSocks5GssapiMethodNecReferenceImpl.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethodNecReferenceImpl.stop();
		}
		if (socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection != null
				&& !socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection.stop();
		}
		if (socksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection != null
				&& !socksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection.stop();
		}
		ThreadHelper.interruptableSleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection04() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection04() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection().newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_04;
		String returningString = echoClient.echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoServer echServer = new EchoServer(newSocks5ClientUsingSocks5GssapiMethod(
				ProtectionLevels.of(
						ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_01;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_02;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_03;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection04() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtection()
						.newSocksNetObjectFactory(), 0);
		String string = TestStringConstants.STRING_04;
		String returningString = EchoServerHelper.startThenEchoThenStop(
				echServer, new EchoClient(), string);
		assertEquals(string, returningString);
	}

}
