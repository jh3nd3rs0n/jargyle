package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;

public class DatagramSocketIT {
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		DatagramSocketEchoHelper.startEchoServer();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		DatagramSocketEchoHelper.stopEchoServer();
		ThreadHelper.sleepForThreeSeconds();
	}

	@Test
	public void testThroughDatagramSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

}