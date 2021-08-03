package com.github.jh3nd3rs0n.jargyle.net;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;

public class DatagramSocketIT {

	@Test
	public void testThroughDatagramSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(string, null);
		assertEquals(string, returningString);
	}

}