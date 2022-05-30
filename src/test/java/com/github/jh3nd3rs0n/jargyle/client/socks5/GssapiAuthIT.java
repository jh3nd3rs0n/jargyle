package com.github.jh3nd3rs0n.jargyle.client.socks5;

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

import com.github.jh3nd3rs0n.jargyle.FilesHelper;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.SocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public class GssapiAuthIT {
	
	private static final String KDC_REALM = "EXAMPLE.COM";
	private static final String KDC_HOST = 
			InetAddress.getLoopbackAddress().getHostAddress();
	private static final int KDC_PORT = 1234;
	
	private static final String ALICE_PRINCIPAL = "alice";
	private static final String RCMD_SERVICE_PRINCIPAL = String.format(
			"rcmd/%s", InetAddress.getLoopbackAddress().getHostAddress());

	private static final String KRB5_CONF_PROPERTY_NAME = 
			"java.security.krb5.conf";
	private static final String USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME = 
			"javax.security.auth.useSubjectCredsOnly";
	private static final String LOGIN_CONFIG_PROPERTY_NAME = 
			"java.security.auth.login.config";
	
	private static Path baseDir = null;
	private static Path aliceKeytab = null;
	private static Path rcmdKeytab = null;
	private static Path loginConf = null;	
	private static Path krb5Conf = null;
	
	private static SimpleKdcServer kerbyServer = null;

	private static Configuration newConfigurationUsingSocks5GssapiAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.GSSAPI))));
	}
	
	private static Configuration newConfigurationUsingSocks5GssapiAuthNecReferenceImpl() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE)));
	}
		
	private static SocksClient newSocks5Client(
			final String host, 
			final Integer port,
			final String serviceName,
			final ProtectionLevels protectionLevels, 
			final boolean necReferenceImpl) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME.newProperty(
						serviceName),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.newProperty(
						protectionLevels),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(necReferenceImpl)));
		return Scheme.SOCKS5.newSocksServerUri(host, port).newSocksClient(
				properties);
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
				
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException, KrbException {
		if (kerbyServer != null) {
			kerbyServer.stop();
			kerbyServer = null;
		}
		if (krb5Conf != null) {
			FilesHelper.attemptsToDeleteIfExists(krb5Conf);
			krb5Conf = null;
		}
		if (aliceKeytab != null) {
			FilesHelper.attemptsToDeleteIfExists(aliceKeytab);
			aliceKeytab = null;
		}
		if (rcmdKeytab != null) {
			FilesHelper.attemptsToDeleteIfExists(rcmdKeytab);
			rcmdKeytab = null;
		}
		if (loginConf != null) {
			FilesHelper.attemptsToDeleteIfExists(loginConf);
			loginConf = null;
		}
		if (baseDir != null) {
			FilesHelper.attemptsToDeleteIfExists(baseDir);
			baseDir = null;
		}
		System.clearProperty(KRB5_CONF_PROPERTY_NAME);
		System.clearProperty(USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME);
		System.clearProperty(LOGIN_CONFIG_PROPERTY_NAME);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(ProtectionLevel.NONE), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						true),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG), 
						false),
				GssapiAuthIT.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}
	
}
