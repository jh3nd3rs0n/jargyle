package jargyle.net;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;

public class DatagramSocketIT {

	@Test
	public void testThroughDatagramSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughDatagramSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(string, null, null);
		assertEquals(string, returningString);
	}

}