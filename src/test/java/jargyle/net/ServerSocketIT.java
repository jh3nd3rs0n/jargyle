package jargyle.net;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;

public class ServerSocketIT {

	@Test
	public void testThroughServerSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughServerSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(string, null, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughServerSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(string, null, null);
		assertEquals(string, returningString);
	}

}
