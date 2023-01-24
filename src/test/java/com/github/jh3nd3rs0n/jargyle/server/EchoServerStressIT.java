package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.IoStressor;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.EchoClient;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

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
 * Then comment out the Ignore annotation for the class EchoServerStressIT below.
 * 
 * You can also adjust the constant value of BACKLOG to the desired number of 
 * backlogged inbound TCP connections.  
 * 
 * You can also adjust the constant value of THREAD_COUNT to the desired 
 * number of threads for testing.
 * 
 */
@org.junit.Ignore
public class EchoServerStressIT {
	
	private static final int BACKLOG = 100;
	private static final int THREAD_COUNT = 100;
	
	@Test
	public void test() throws IOException {
		EchoServer echoServer = new EchoServer(
				NetObjectFactory.getDefault(), 
				EchoServer.PORT,
				BACKLOG);
		try {
			echoServer.start();
			IoStressor ioStressor = new IoStressor(THREAD_COUNT);
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new EchoClient().echoThroughNewSocket(
						TestStringConstants.STRING_01, null);
			});
			stats.print();
			assertEquals(THREAD_COUNT, stats.getActualSuccessfulThreadCount());
		} finally {
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
			ThreadHelper.sleepForThreeSeconds();
		}
	}
}
