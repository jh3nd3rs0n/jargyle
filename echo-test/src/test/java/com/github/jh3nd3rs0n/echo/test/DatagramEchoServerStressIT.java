package com.github.jh3nd3rs0n.echo.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;

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
 * Then comment out the Ignore annotation for the class DatagramEchoServerStressIT 
 * below.
 * 
 * You can also adjust the constant value of THREAD_COUNT to the desired 
 * number of threads for testing.
 * 
 */
@org.junit.Ignore
public class DatagramEchoServerStressIT {
	
	private static final int THREAD_COUNT = 500;
	
	@Test
	public void test() throws IOException {
		DatagramEchoServer datagramEchoServer = new DatagramEchoServer();
		try {
			datagramEchoServer.start();
			IoStressor ioStressor = new IoStressor(THREAD_COUNT);
			IoStressor.Stats stats = ioStressor.executeForEachThread(() -> {
				new DatagramEchoClient().echo(TestStringConstants.STRING_01);
			});
			stats.print();
			assertEquals(THREAD_COUNT, stats.getActualSuccessfulThreadCount());
		} finally {
			if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
				datagramEchoServer.stop();
			}
		}
	}
	
}
