package com.github.jh3nd3rs0n.echo;

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

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SchemeConstants;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;

public class EchoThroughSocksServerUsingSocks5GssapiauthIT {
	
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
	
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIAUTH = 8100;
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 8200;
	
	private static Path baseDir = null;
	private static Path aliceKeytab = null;
	private static Path rcmdKeytab = null;
	private static Path loginConf = null;	
	private static Path krb5Conf = null;
	
	private static SimpleKdcServer kerbyServer = null;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;

	private static SocksServer socksServerUsingSocks5Gssapiauth;
	private static SocksServer socksServerUsingSocks5GssapiauthNecReferenceImpl;
	
	private static Configuration newConfigurationUsingSocks5Gssapiauth() {
		return Configuration.newUnmodifiableInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIAUTH)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.GSSAPI))));
	}
	
	private static Configuration newConfigurationUsingSocks5GssapiauthNecReferenceImpl() {
		return Configuration.newUnmodifiableInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(
								SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE)));
	}
	
	private static SocksClient newSocks5ClientUsingSocks5Gssapiauth(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.newProperty(
						protectionLevels),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(false)));
		return SchemeConstants.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIAUTH)
				.newSocksClient(properties);		
	}
	
	private static SocksClient newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.newProperty(
						protectionLevels),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(true)));
		return SchemeConstants.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SOCKS_SERVER_PORT_USING_SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL)
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
		socksServerUsingSocks5Gssapiauth = new SocksServer(
				newConfigurationUsingSocks5Gssapiauth());
		socksServerUsingSocks5Gssapiauth.start();
		socksServerUsingSocks5GssapiauthNecReferenceImpl = new SocksServer(
				newConfigurationUsingSocks5GssapiauthNecReferenceImpl());
		socksServerUsingSocks5GssapiauthNecReferenceImpl.start();
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
		if (!socksServerUsingSocks5Gssapiauth.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5Gssapiauth.stop();
		}
		if (!socksServerUsingSocks5GssapiauthNecReferenceImpl.getState().equals(SocksServer.State.STOPPED)) {
			socksServerUsingSocks5GssapiauthNecReferenceImpl.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5Gssapiauth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5Gssapiauth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5Gssapiauth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5Gssapiauth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5Gssapiauth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5Gssapiauth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClientBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5Gssapiauth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5Gssapiauth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5Gssapiauth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImpl03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthNecReferenceImplWithIntegProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5GssapiauthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthWithIntegAndConfProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection02() throws IOException {
		EchoServer echServer = new EchoServer(newSocks5ClientUsingSocks5Gssapiauth(
				ProtectionLevels.newInstance(
						ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}

	@Test
	public void testEchoServerBehindSocks5ServerUsingSocks5GssapiauthWithIntegProtection03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientUsingSocks5Gssapiauth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
}
