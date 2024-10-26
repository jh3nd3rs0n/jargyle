package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;
import com.github.jh3nd3rs0n.jargyle.test.help.net.TestServer;
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

	private static DatagramTestServer echoDatagramTestServer;
	private static int echoDatagramTestServerPort;
	private static TestServer echoTestServer;
	private static int echoTestServerPort;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		echoDatagramTestServer = EchoDatagramTestServerHelper.newEchoDatagramTestServer(0);
		echoDatagramTestServer.start();
		echoDatagramTestServerPort = echoDatagramTestServer.getPort();
		echoTestServer = EchoTestServerHelper.newEchoTestServer(0);
		echoTestServer.start();
		echoTestServerPort = echoTestServer.getPort();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (echoDatagramTestServer != null
				&& !echoDatagramTestServer.getState().equals(DatagramTestServer.State.STOPPED)) {
			echoDatagramTestServer.stop();
		}
		if (echoTestServer != null
				&& !echoTestServer.getState().equals(TestServer.State.STOPPED)) {
			echoTestServer.stop();
		}		
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

	@Test
	public void testEchoDatagramTestClient01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoDatagramTestClient().echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClient02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoDatagramTestClient().echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClient03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoDatagramTestClient().echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClient04() throws IOException {
		String string = TestStringConstants.STRING_04;
		String returningString = new EchoDatagramTestClient().echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramTestClient05() throws IOException {
		String string = TestStringConstants.STRING_05;
		String returningString = new EchoDatagramTestClient().echo(string, echoDatagramTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClient01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new EchoTestClient().echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClient02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new EchoTestClient().echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClient03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new EchoTestClient().echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClient04() throws IOException {
		String string = TestStringConstants.STRING_04;
		String returningString = new EchoTestClient().echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoTestClient05() throws IOException {
		String string = TestStringConstants.STRING_05;
		String returningString = new EchoTestClient().echo(string, echoTestServerPort);
		assertEquals(string, returningString);
	}

}
