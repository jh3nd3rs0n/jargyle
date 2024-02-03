package com.github.jh3nd3rs0n.echo.integration.test;

import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.server.SimpleKdcServer;
import org.apache.kerby.kerberos.kerb.server.impl.DefaultInternalKdcServerImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;

public class EchoThroughSocks5ClientToSocksServerUsingSocks5GssapiMethodIT {
	
	private static final String KDC_REALM = "EXAMPLE.COM";
	private static final String KDC_HOST = 
			InetAddress.getLoopbackAddress().getHostAddress();
	private static final int KDC_PORT = 8000;
	
	private static final String ALICE_PRINCIPAL = "alice";
	private static final String RCMD_SERVICE_PRINCIPAL = String.format(
			"rcmd/%s", InetAddress.getLoopbackAddress().getHostAddress());

	private static final String KRB5_CONF_PROPERTY_NAME = 
			"java.security.krb5.conf";
	private static final String USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME = 
			"javax.security.auth.useSubjectCredsOnly";
	private static final String LOGIN_CONFIG_PROPERTY_NAME = 
			"java.security.auth.login.config";
	
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIMETHOD = 8100;
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL = 8200;
	
	private static Path baseDir = null;
	private static Path aliceKeytab = null;
	private static Path rcmdKeytab = null;
	private static Path loginConf = null;	
	private static Path krb5Conf = null;
	
	private static SimpleKdcServer kerbyServer = null;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;

	private static SocksServer socksServerUsingSocks5GssapiMethod;
	private static SocksServer socksServerUsingSocks5GssapiMethodNecReferenceImpl;
	
	private static Configuration newConfigurationUsingSocks5GssapiMethod() {
		return Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIMETHOD)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI))));
	}
	
	private static Configuration newConfigurationUsingSocks5GssapiMethodNecReferenceImpl() {
		return Configuration.newUnmodifiableInstance(Settings.of(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.valueOf(
								SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.of(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE)));
	}
	
	private static SocksClient newSocks5ClientUsingSocks5GssapiMethod(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
						protectionLevels),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(false)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIMETHOD)
				.newSocksClient(properties);		
	}
	
	private static SocksClient newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.of(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.of(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.newProperty(
						protectionLevels),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(true)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL)
				.newSocksClient(properties);		
	}
	@BeforeClass
	public static void setUpBeforeClass() throws IOException, KrbException {
		
		baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		loginConf = Files.createFile(baseDir.resolve("login.conf"));
		aliceKeytab = baseDir.resolve("alice.keytab");
		rcmdKeytab = baseDir.resolve("rmcd.keytab");
		krb5Conf = baseDir.resolve("krb5.conf");
		
		SimpleKdcServer kerbyServer = new SimpleKdcServer();
		kerbyServer.setKdcRealm(KDC_REALM);
		kerbyServer.setKdcHost(KDC_HOST);
		kerbyServer.setKdcPort(KDC_PORT);
		kerbyServer.setWorkDir(baseDir.toFile());
		kerbyServer.setInnerKdcImpl(new DefaultInternalKdcServerImpl(
				kerbyServer.getKdcSetting()));
		kerbyServer.init();
		kerbyServer.createPrincipal(ALICE_PRINCIPAL, "alice");
		kerbyServer.exportPrincipal(ALICE_PRINCIPAL, aliceKeytab.toFile());
		kerbyServer.createPrincipal(RCMD_SERVICE_PRINCIPAL, "rcmd");
		kerbyServer.exportPrincipal(RCMD_SERVICE_PRINCIPAL, rcmdKeytab.toFile());
		kerbyServer.start();
		
		String aliceKeytabUriString = aliceKeytab.toUri().toString();
		String rcmdKeytabUriString = rcmdKeytab.toUri().toString();
		
		FileWriter w = new FileWriter(loginConf.toFile());
		w.write("com.sun.security.jgss.initiate {\n");
		w.write("  com.sun.security.auth.module.Krb5LoginModule required\n");
		w.write(String.format(
				"  principal=\"%s\"\n", ALICE_PRINCIPAL));		
		w.write("  useKeyTab=true\n");
		w.write(String.format(
				"  keyTab=\"%s\"\n", aliceKeytabUriString));
		w.write("  storeKey=true;\n");
		w.write("};\n");
		w.write("com.sun.security.jgss.accept {\n");
		w.write("  com.sun.security.auth.module.Krb5LoginModule required\n");
		w.write(String.format(
				"  principal=\"%s\"\n", RCMD_SERVICE_PRINCIPAL));		
		w.write("  useKeyTab=true\n");
		w.write(String.format(
				"  keyTab=\"%s\"\n", rcmdKeytabUriString));
		w.write("  storeKey=true;\n");
		w.write("};\n");
		w.flush();
		w.close();
		
		System.setProperty(
				KRB5_CONF_PROPERTY_NAME, krb5Conf.toAbsolutePath().toString());
		System.setProperty(USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME, "false");
		System.setProperty(
				LOGIN_CONFIG_PROPERTY_NAME, loginConf.toAbsolutePath().toString());
		
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
		echoServer = new EchoServer();
		echoServer.start();
		socksServerUsingSocks5GssapiMethod = new SocksServer(
				newConfigurationUsingSocks5GssapiMethod());
		socksServerUsingSocks5GssapiMethod.start();
		socksServerUsingSocks5GssapiMethodNecReferenceImpl = new SocksServer(
				newConfigurationUsingSocks5GssapiMethodNecReferenceImpl());
		socksServerUsingSocks5GssapiMethodNecReferenceImpl.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException, KrbException {
		if (kerbyServer != null) {
			kerbyServer.stop();
			kerbyServer = null;
		}
		if (krb5Conf != null) {
			Files.deleteIfExists(krb5Conf);
			krb5Conf = null;
		}
		if (aliceKeytab != null) {
			Files.deleteIfExists(aliceKeytab);
			aliceKeytab = null;
		}
		if (rcmdKeytab != null) {
			Files.deleteIfExists(rcmdKeytab);
			rcmdKeytab = null;
		}
		if (loginConf != null) {
			Files.deleteIfExists(loginConf);
			loginConf = null;
		}
		if (baseDir != null) {
			Files.deleteIfExists(baseDir);
			baseDir = null;
		}
		System.clearProperty(KRB5_CONF_PROPERTY_NAME);
		System.clearProperty(USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME);
		System.clearProperty(LOGIN_CONFIG_PROPERTY_NAME);
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}
		if (!socksServerUsingSocks5GssapiMethod.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethod.stop();
		}
		if (!socksServerUsingSocks5GssapiMethodNecReferenceImpl.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiMethodNecReferenceImpl.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethod03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImpl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethodNecReferenceImpl(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiMethod(
						ProtectionLevels.of(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
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
	
}
