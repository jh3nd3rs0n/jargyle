package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;

public class ServerSocketIT {
	
	@Test
	public void testThroughServerSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughServerSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(string, null);
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughServerSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(string, null);
		assertEquals(string, returningString);
	}

}
