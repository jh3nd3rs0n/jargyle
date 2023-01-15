package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

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

	private static final int THREAD_COUNT = 100;
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		DatagramSocketEchoHelper.startEchoServer();
		SocketEchoHelper.startEchoServer();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		DatagramSocketEchoHelper.stopEchoServer();
		SocketEchoHelper.stopEchoServer();
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramSockets() {
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
								TestStringConstants.STRING_01, null);
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
	public void testSockets() {
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
								TestStringConstants.STRING_01, null);
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
