package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoIT {

	private static DatagramEchoServer datagramEchoServer;
	private static int datagramEchoServerPort;
	private static EchoServer echoServer;
	private static int echoServerPort;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer(0);
		datagramEchoServer.start();
		datagramEchoServerPort = datagramEchoServer.getPort();
		echoServer = new EchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (datagramEchoServer != null
				&& !datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}		
		ThreadHelper.interruptableSleepForThreeSeconds();
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

}
