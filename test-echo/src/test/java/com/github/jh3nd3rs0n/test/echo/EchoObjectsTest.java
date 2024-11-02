package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.test.help.net.Server;
import com.github.jh3nd3rs0n.test.help.string.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoObjectsTest {

	private static DatagramServer datagramEchoServer;
	private static int datagramEchoServerPort;
	private static Server echoServer;
	private static int echoServerPort;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = DatagramEchoServerHelper.newDatagramEchoServer(0);
		datagramEchoServer.start();
		datagramEchoServerPort = datagramEchoServer.getPort();
		echoServer = EchoServerHelper.newEchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (datagramEchoServer != null
				&& !datagramEchoServer.getState().equals(DatagramServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(Server.State.STOPPED)) {
			echoServer.stop();
		}		
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

	@Test
	public void testDatagramEchoClient01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new DatagramEchoClient().echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClient02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new DatagramEchoClient().echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClient03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new DatagramEchoClient().echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClient04() throws IOException {
		String string = TestStringConstants.STRING_04;
		String returningString = new DatagramEchoClient().echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClient05() throws IOException {
		String string = TestStringConstants.STRING_05;
		String returningString = new DatagramEchoClient().echo(string, datagramEchoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient04() throws IOException {
		String string = TestStringConstants.STRING_04;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient05() throws IOException {
		String string = TestStringConstants.STRING_05;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

}
