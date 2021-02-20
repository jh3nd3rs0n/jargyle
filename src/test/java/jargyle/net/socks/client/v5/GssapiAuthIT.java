package jargyle.net.socks.client.v5;

import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.server.SimpleKdcServer;
import org.apache.kerby.kerberos.kerb.server.impl.DefaultInternalKdcServerImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import jargyle.NetConstants;
import jargyle.TestStringConstants;
import jargyle.net.DatagramSocketIT;
import jargyle.net.ServerSocketIT;
import jargyle.net.SocketIT;
import jargyle.net.socks.client.SocksClientHelper;
import jargyle.net.socks.server.ConfigurationHelper;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevel;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevels;

public class GssapiAuthIT {
	
	private static final String KDC_HOST = NetConstants.LOOPBACK_ADDRESS.getHostAddress();
	private static final String KDC_REALM = "EXAMPLE.COM";
	
	private static final int KDC_TCP_PORT = 54321;
	private static final int KDC_UDP_PORT = 54321;
	
	private static final String ALICE_PRINCIPAL = "alice";
	private static final String RCMD_SERVICE_PRINCIPAL = String.format(
			"rcmd/%s", NetConstants.LOOPBACK_ADDRESS.getHostAddress());

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

	@BeforeClass
	public static void setUpBeforeClass() throws IOException, KrbException {
		
		baseDir = Files.createTempDirectory("jargyle-");
		loginConf = Files.createFile(baseDir.resolve("login.conf"));
		aliceKeytab = baseDir.resolve("alice.keytab");
		rcmdKeytab = baseDir.resolve("rmcd.keytab");
		krb5Conf = baseDir.resolve("krb5.conf");
		
		SimpleKdcServer kerbyServer = new SimpleKdcServer();
		kerbyServer.setKdcHost(KDC_HOST);
		kerbyServer.setKdcRealm(KDC_REALM);
		kerbyServer.setKdcTcpPort(KDC_TCP_PORT);
		kerbyServer.setAllowUdp(true);
		kerbyServer.setKdcUdpPort(KDC_UDP_PORT);
		kerbyServer.setWorkDir(baseDir.toFile());
		kerbyServer.setInnerKdcImpl(new DefaultInternalKdcServerImpl(kerbyServer.getKdcSetting()));
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
		
		System.setProperty(KRB5_CONF_PROPERTY_NAME, krb5Conf.toAbsolutePath().toString());
		System.setProperty(USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME, "false");
		System.setProperty(LOGIN_CONFIG_PROPERTY_NAME, loginConf.toAbsolutePath().toString());
				
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
	}
	
	
	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}
	
}
