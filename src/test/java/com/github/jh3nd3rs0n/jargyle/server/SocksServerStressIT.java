package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.ResourceHelper;
import com.github.jh3nd3rs0n.jargyle.ResourceNameConstants;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.DatagramEchoClient;
import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.EchoClient;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUserRepository;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

/*
 * This integration test is ignored due to its success being dependent on the 
 * machine running this integration test. 
 * 
 * To run this integration test only, add the following configuration XML 
 * element within the Maven Failsafe plugin in the file pom.xml 
 * 
 * <plugin>
 *     <groupId>org.apache.maven.plugins</groupId>
 *     <artifactId>maven-failsafe-plugin</artifactId>
 *     <!-- ... -->
 *     <configuration>
 *         <!-- enable virtual threads in Java 19 -->
 *         <!--
 *         <argLine>--enable-preview</argLine>
 *         -->
 *         <includes>
 */
 //            <include>**/*StressIT.java</include>
/* 
 *         </includes>
 *     </configuration>
 *     <!-- ... -->
 * <plugin>
 * 
 * Then comment out the Ignore annotation for the class SocksServerStressIT below.
 * 
 * You can also adjust the constant value of BACKLOG to the desired number of 
 * backlogged inbound TCP connections.  
 * 
 * You can also adjust the constant values of DATAGRAM_ECHO_CLIENT_THREAD_COUNT 
 * and ECHO_CLIENT_THREAD_COUNT to the desired number of threads for testing.
 * 
 */
@org.junit.Ignore
public class SocksServerStressIT {

	private static final int BACKLOG = 100;
	
	private static final int DATAGRAM_ECHO_CLIENT_THREAD_COUNT = 100;
	private static final int ECHO_CLIENT_THREAD_COUNT = 100;
	
	private static final int SOCKS_SERVER_PORT = 6789;
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH = 7890;
	private static final int SOCKS_SERVER_PORT_USING_SSL = 8900;
	private static final int SOCKS_SERVER_PORT_USING_SSL_AND_SOCKS5_USERPASSAUTH = 9000;
	private static final String SOCKS5_USERS = new StringBuilder()
			.append("Aladdin:opensesame ")
			.append("Jasmine:mission%3Aimpossible ")
			.append("Abu:safeDriversSave40%25")
			.toString();
	
	private static Configuration newConfiguration() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(SOCKS_SERVER_PORT)),
				GeneralSettingSpecConstants.BACKLOG.newSetting(
						NonnegativeInteger.newInstance(BACKLOG))));
	}
	
	private static Configuration newConfigurationUsingSocks5Userpassauth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH)),
				GeneralSettingSpecConstants.BACKLOG.newSetting(
						NonnegativeInteger.newInstance(BACKLOG)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
						new StringSourceUserRepository(SOCKS5_USERS))));
	}
	
	private static Configuration newConfigurationUsingSsl() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(SOCKS_SERVER_PORT_USING_SSL)),
				GeneralSettingSpecConstants.BACKLOG.newSetting(
						NonnegativeInteger.newInstance(BACKLOG)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE))));
	}
	
	private static Configuration newConfigurationUsingSslAndSocks5Userpassauth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(SOCKS_SERVER_PORT_USING_SSL_AND_SOCKS5_USERPASSAUTH)),
				GeneralSettingSpecConstants.BACKLOG.newSetting(
						NonnegativeInteger.newInstance(BACKLOG)),
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
						new StringSourceUserRepository(SOCKS5_USERS))));
	}
	
	private static SocksClient newSocks5Client() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT))
				.newSocksClient(Properties.newInstance());
	}
	
	private static SocksClient newSocks5ClientUsingSocks5Userpassauth(
			final String username,
			final char[] password) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
						username),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
						EncryptedPassword.newInstance(password)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(properties);		
	}	
	
	private static SocksClient newSocks5ClientUsingSsl() {
		Properties properties = Properties.newInstance(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_USING_SSL))
				.newSocksClient(properties);
	}
	
	private static SocksClient newSocks5ClientUsingSslAndSocks5Userpassauth(
			final String username,
			final char[] password) {
		Properties properties = Properties.newInstance(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
						username),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
						EncryptedPassword.newInstance(password)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_USING_SSL_AND_SOCKS5_USERPASSAUTH))
				.newSocksClient(properties);		
	}
	
	//@org.junit.Ignore
	@Test
	public void testSocks5DatagramSockets() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServer = new SocksServer(newConfiguration());
		DatagramEchoServer datagramEchoServer = new DatagramEchoServer();
		try {
			socksServer.start();			
			datagramEchoServer.start();
			IoStressor ioStressor = new IoStressor(DATAGRAM_ECHO_CLIENT_THREAD_COUNT);			
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new DatagramEchoClient().echoThroughNewDatagramSocket(
						TestStringConstants.STRING_01, 
						newSocks5Client().newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(DATAGRAM_ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());
		} finally {
			if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
				datagramEchoServer.stop();
			}
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}	
	
	//@org.junit.Ignore
	@Test
	public void testSocks5DatagramSocketsUsingSocks5Userpassauth() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServerUsingSocks5Userpassauth = new SocksServer(
				newConfigurationUsingSocks5Userpassauth());
		DatagramEchoServer datagramEchoServer = new DatagramEchoServer();
		try {
			socksServerUsingSocks5Userpassauth.start();			
			datagramEchoServer.start();			
			IoStressor ioStressor = new IoStressor(DATAGRAM_ECHO_CLIENT_THREAD_COUNT);
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new DatagramEchoClient().echoThroughNewDatagramSocket(
						TestStringConstants.STRING_01, 
						newSocks5ClientUsingSocks5Userpassauth(
								"Aladdin",
								"opensesame".toCharArray()).newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(DATAGRAM_ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());			
		} finally {
			if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
				datagramEchoServer.stop();
			}
			if (!socksServerUsingSocks5Userpassauth.getState().equals(SocksServer.State.STOPPED)) {
				socksServerUsingSocks5Userpassauth.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}
	
	//@org.junit.Ignore
	@Test
	public void testSocks5DatagramSocketsUsingSsl() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServerUsingSsl = new SocksServer(
				newConfigurationUsingSsl());
		DatagramEchoServer datagramEchoServer = new DatagramEchoServer();
		try {
			socksServerUsingSsl.start();			
			datagramEchoServer.start();
			IoStressor ioStressor = new IoStressor(DATAGRAM_ECHO_CLIENT_THREAD_COUNT);			
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new DatagramEchoClient().echoThroughNewDatagramSocket(
						TestStringConstants.STRING_01, 
						newSocks5ClientUsingSsl().newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(DATAGRAM_ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());			
		} finally {
			if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
				datagramEchoServer.stop();
			}
			if (!socksServerUsingSsl.getState().equals(SocksServer.State.STOPPED)) {
				socksServerUsingSsl.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}
	
	//@org.junit.Ignore
	@Test
	public void testSocks5DatagramSocketsUsingSslAndSocks5Userpassauth() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServerUsingSslAndSocks5Userpassauth = new SocksServer(
				newConfigurationUsingSslAndSocks5Userpassauth());
		DatagramEchoServer datagramEchoServer = new DatagramEchoServer();
		try {
			socksServerUsingSslAndSocks5Userpassauth.start();
			datagramEchoServer.start();
			IoStressor ioStressor = new IoStressor(DATAGRAM_ECHO_CLIENT_THREAD_COUNT);
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new DatagramEchoClient().echoThroughNewDatagramSocket(
						TestStringConstants.STRING_01, 
						newSocks5ClientUsingSslAndSocks5Userpassauth(
								"Aladdin",
								"opensesame".toCharArray()).newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(DATAGRAM_ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());			
		} finally {
			if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
				datagramEchoServer.stop();
			}
			if (!socksServerUsingSslAndSocks5Userpassauth.getState().equals(SocksServer.State.STOPPED)) {
				socksServerUsingSslAndSocks5Userpassauth.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
		
	}

	//@org.junit.Ignore
	@Test
	public void testSocks5Sockets() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServer = new SocksServer(newConfiguration());
		EchoServer echoServer = new EchoServer(
				NetObjectFactory.getDefault(), EchoServer.PORT, BACKLOG);
		try {
			socksServer.start();			
			echoServer.start();			
			IoStressor ioStressor = new IoStressor(ECHO_CLIENT_THREAD_COUNT);			
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new EchoClient().echoThroughNewSocket(
						TestStringConstants.STRING_01, 
						newSocks5Client().newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());			
		} finally {
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}	
	
	//@org.junit.Ignore
	@Test
	public void testSocks5SocketsUsingSocks5Userpassauth() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServerUsingSocks5Userpassauth = new SocksServer(
				newConfigurationUsingSocks5Userpassauth());
		EchoServer echoServer = new EchoServer(
				NetObjectFactory.getDefault(), EchoServer.PORT, BACKLOG);
		try {
			socksServerUsingSocks5Userpassauth.start();
			echoServer.start();			
			IoStressor ioStressor = new IoStressor(ECHO_CLIENT_THREAD_COUNT);			
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new EchoClient().echoThroughNewSocket(
						TestStringConstants.STRING_01, 
						newSocks5ClientUsingSocks5Userpassauth(
								"Aladdin",
								"opensesame".toCharArray()).newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());			
		} finally {
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
			if (!socksServerUsingSocks5Userpassauth.getState().equals(SocksServer.State.STOPPED)) {
				socksServerUsingSocks5Userpassauth.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}
	
	//@org.junit.Ignore
	@Test
	public void testSocks5SocketsUsingSsl() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServerUsingSsl = new SocksServer(
				newConfigurationUsingSsl());
		EchoServer echoServer = new EchoServer(
				NetObjectFactory.getDefault(), EchoServer.PORT, BACKLOG);
		try {
			socksServerUsingSsl.start();
			echoServer.start();			
			IoStressor ioStressor = new IoStressor(ECHO_CLIENT_THREAD_COUNT);			
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new EchoClient().echoThroughNewSocket(
						TestStringConstants.STRING_01, 
						newSocks5ClientUsingSsl().newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());			
		} finally {
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
			if (!socksServerUsingSsl.getState().equals(SocksServer.State.STOPPED)) {
				socksServerUsingSsl.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}
	
	//@org.junit.Ignore
	@Test
	public void testSocks5SocketsUsingSslAndSocks5Userpassauth() throws IOException {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());
		SocksServer socksServerUsingSslAndSocks5Userpassauth = new SocksServer(
				newConfigurationUsingSslAndSocks5Userpassauth());
		EchoServer echoServer = new EchoServer(
				NetObjectFactory.getDefault(), EchoServer.PORT, BACKLOG);
		try {
			socksServerUsingSslAndSocks5Userpassauth.start();
			echoServer.start();			
			IoStressor ioStressor = new IoStressor(ECHO_CLIENT_THREAD_COUNT);			
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new EchoClient().echoThroughNewSocket(
						TestStringConstants.STRING_01, 
						newSocks5ClientUsingSslAndSocks5Userpassauth(
								"Aladdin",
								"opensesame".toCharArray()).newSocksNetObjectFactory());
			});
			stats.print();
			assertEquals(ECHO_CLIENT_THREAD_COUNT, stats.getActualSuccessfulThreadCount());			
		} finally {
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
			if (!socksServerUsingSslAndSocks5Userpassauth.getState().equals(SocksServer.State.STOPPED)) {
				socksServerUsingSslAndSocks5Userpassauth.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}
	
}
