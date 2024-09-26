package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.ThreadHelper;
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
	private static EchoServer echoServer;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(5, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
		echoServer = new EchoServer();
		echoServer.start();		
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}		
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClient01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new DatagramEchoClient().echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClient02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new DatagramEchoClient().echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClient03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new DatagramEchoClient().echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClient01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoClient().echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoClient().echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoClient().echo(string);
		assertEquals(string, returningString);
	}
	
}
