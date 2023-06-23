package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;

public class DatagramSocketIT {
	
	private static DatagramEchoServer datagramEchoServer;
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer();
		datagramEchoServer.start();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		ThreadHelper.sleepForThreeSeconds();
	}

	@Test
	public void testThroughDatagramSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = new DatagramEchoClient().echoThroughNewDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

}