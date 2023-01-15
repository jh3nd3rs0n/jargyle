package com.github.jh3nd3rs0n.jargyle.client.socks5;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.ResourceHelper;
import com.github.jh3nd3rs0n.jargyle.ResourceNameConstants;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServerHelper;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUserRepository;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

/*
 * This integration test is ignored due to its long testing time. 
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
 //            <include>**/StressIT.java</include>
/* 
 *         </includes>
 *     </configuration>
 *     <!-- ... -->
 * <plugin>
 * 
 * Then comment out the Ignore annotation for the class StressIT below.
 * 
 * You can also adjust the constant value of THREAD_COUNT to the desired 
 * number of threads for testing.
 * 
 * 100 are the number of threads (virtual and platform) that can run 
 * successfully on my machine.
 * 
 */
@org.junit.Ignore
public class StressIT {
	
	private static final int BACKLOG = 65535;
	
	private static final int SOCKS_SERVER_PORT = 6789;
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH = 7890;
	private static final int SOCKS_SERVER_PORT_USING_SSL = 8900;
	private static final int SOCKS_SERVER_PORT_USING_SSL_AND_SOCKS5_USERPASSAUTH = 9000;
	
	private static final int THREAD_COUNT = 100;
	
	private static List<SocksServer> socksServers;
	private static List<SocksServer> socksServersUsingSocks5Userpassauth;
	private static List<SocksServer> socksServersUsingSsl;
	private static List<SocksServer> socksServersUsingSslAndSocks5Userpassauth;
	
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
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		DatagramSocketEchoHelper.startEchoServer();
		SocketEchoHelper.startEchoServer();
		socksServers = SocksServerHelper.newStartedSocksServers(Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT)),
						GeneralSettingSpecConstants.BACKLOG.newSetting(
								NonnegativeInteger.newInstance(BACKLOG))))));
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame ");
		sb.append("Jasmine:mission%3Aimpossible ");
		sb.append("Abu:safeDriversSave40%25");
		socksServersUsingSocks5Userpassauth = SocksServerHelper.newStartedSocksServers(Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH)),
						GeneralSettingSpecConstants.BACKLOG.newSetting(
								NonnegativeInteger.newInstance(BACKLOG)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(sb.toString()))))));
		socksServersUsingSsl = SocksServerHelper.newStartedSocksServers(Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
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
										ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE))))));
		socksServersUsingSslAndSocks5Userpassauth = SocksServerHelper.newStartedSocksServers(Arrays.asList(
				ImmutableConfiguration.newInstance(Settings.newInstance(
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
								new StringSourceUserRepository(sb.toString()))))));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		DatagramSocketEchoHelper.stopEchoServer();
		SocketEchoHelper.stopEchoServer();
		SocksServerHelper.stopSocksServers(socksServers);
		SocksServerHelper.stopSocksServers(socksServersUsingSocks5Userpassauth);
		SocksServerHelper.stopSocksServers(socksServersUsingSsl);
		SocksServerHelper.stopSocksServers(socksServersUsingSslAndSocks5Userpassauth);
		ThreadHelper.sleepForThreeSeconds();
	}

	@Test
	public void testSocks5DatagramSockets() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						DatagramSocketEchoHelper.echoThroughDatagramSocket(
								TestStringConstants.STRING_01, 
								newSocks5Client().newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}	
	
	@Test
	public void testSocks5DatagramSocketsUsingSocks5Userpassauth() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						DatagramSocketEchoHelper.echoThroughDatagramSocket(
								TestStringConstants.STRING_01, 
								newSocks5ClientUsingSocks5Userpassauth(
										"Aladdin",
										"opensesame".toCharArray()).newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}
	
	@Test
	public void testSocks5DatagramSocketsUsingSsl() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						DatagramSocketEchoHelper.echoThroughDatagramSocket(
								TestStringConstants.STRING_01, 
								newSocks5ClientUsingSsl().newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}
	
	@Test
	public void testSocks5DatagramSocketsUsingSslAndSocks5Userpassauth() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						DatagramSocketEchoHelper.echoThroughDatagramSocket(
								TestStringConstants.STRING_01, 
								newSocks5ClientUsingSslAndSocks5Userpassauth(
										"Aladdin",
										"opensesame".toCharArray()).newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}

	@Test
	public void testSocks5Sockets() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						SocketEchoHelper.echoThroughSocket(
								TestStringConstants.STRING_01, 
								newSocks5Client().newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}	
	
	@Test
	public void testSocks5SocketsUsingSocks5Userpassauth() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						SocketEchoHelper.echoThroughSocket(
								TestStringConstants.STRING_01, 
								newSocks5ClientUsingSocks5Userpassauth(
										"Aladdin",
										"opensesame".toCharArray()).newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}
	
	@Test
	public void testSocks5SocketsUsingSsl() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						SocketEchoHelper.echoThroughSocket(
								TestStringConstants.STRING_01, 
								newSocks5ClientUsingSsl().newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}
	
	@Test
	public void testSocks5SocketsUsingSslAndSocks5Userpassauth() {
		System.out.printf("Running %s%n", Thread.currentThread().getStackTrace()[1].getMethodName());		
		ReentrantLock lock = new ReentrantLock();
		Set<String> exceptions = new TreeSet<String>();
		Set<Long> completedTimes = new TreeSet<Long>();
		AtomicInteger actualThreadCount = new AtomicInteger(0);
		AtomicInteger actualSuccessfulThreadCount = new AtomicInteger(0);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			IntStream.range(0, THREAD_COUNT).forEach(i -> {
				executor.execute(() -> {
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						SocketEchoHelper.echoThroughSocket(
								TestStringConstants.STRING_01, 
								newSocks5ClientUsingSslAndSocks5Userpassauth(
										"Aladdin",
										"opensesame".toCharArray()).newSocksNetObjectFactory());
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					lock.lock();
					try {
						if (ioe != null) {
							exceptions.add(ioe.toString());
						}
						completedTimes.add(Long.valueOf(completedTime));
					} finally {
						lock.unlock();
					}
					actualThreadCount.incrementAndGet();
					if (ioe == null) {
						actualSuccessfulThreadCount.incrementAndGet();
					}
				});
			});
		} finally {
			try {
				executor.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Long> completedTimesList = 
				completedTimes.stream().sorted().collect(Collectors.toList());
		int completedTimesListSize = completedTimesList.size();
		long combinedTime = 0L;
		for (Long completedTime : completedTimesList) {
			combinedTime += completedTime.longValue();
		}
		long shortestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(0).longValue() : 0L;
		long averageTime = (completedTimesListSize > 0) ? 
				Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
		long longestTime = (completedTimesListSize > 0) ? 
				completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
		System.out.printf("# of threads: %s%n", THREAD_COUNT);
		System.out.printf("Actual # of threads: %s%n", actualThreadCount);
		System.out.printf("Actual # of successful threads: %s%n", 
				actualSuccessfulThreadCount);
		System.out.printf("Common IOExceptions: %s%n", 
				exceptions.stream().collect(Collectors.joining(", ")));
		System.out.printf("Combined time: %s ms%n", combinedTime);
		System.out.printf("Shortest time: %s ms%n", shortestTime);
		System.out.printf("Average time: %s ms%n", averageTime);
		System.out.printf("Longest time: %s ms%n", longestTime);
		assertEquals(THREAD_COUNT, actualSuccessfulThreadCount.intValue());
	}
	
}
