package jargyle.server.socks5;

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

import jargyle.TestStringConstants;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevel;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.server.ConfigurationHelper;
import jargyle.server.DatagramSocketInterfaceIT;
import jargyle.server.ServerSocketInterfaceIT;
import jargyle.server.SocketInterfaceIT;

public class GssapiAuthIT {
	
	private static final InetAddress LOOPBACK_ADDRESS = 
			InetAddress.getLoopbackAddress();
	
	private static final String KDC_HOST = LOOPBACK_ADDRESS.getHostAddress();
	private static final String KDC_REALM = "EXAMPLE.COM";
	
	private static final int KDC_TCP_PORT = 54321;
	private static final int KDC_UDP_PORT = 54321;
	
	private static final String ALICE_PRINCIPAL = "alice";
	private static final String RCMD_SERVICE_PRINCIPAL = String.format(
			"rcmd/%s", LOOPBACK_ADDRESS.getHostAddress());

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
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.NONE), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), true),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuthNecReferenceImpl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingGssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						RCMD_SERVICE_PRINCIPAL,
						GssapiProtectionLevels.newInstance(GssapiProtectionLevel.REQUIRED_INTEG), false),
				ConfigurationHelper.newConfigurationUsingSocks5GssapiAuth());
		assertEquals(string, returningString);
	}
	
}
