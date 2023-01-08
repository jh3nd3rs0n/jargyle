package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;

public class SocketIT {

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		SocketEchoHelper.startEchoServer();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		SocketEchoHelper.stopEchoServer();
	}
	
	@Test
	public void testThroughSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(string, null);
		assertEquals(string, returningString);
	}

}