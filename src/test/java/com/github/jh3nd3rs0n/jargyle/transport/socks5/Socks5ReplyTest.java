package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Socks5ReplyTest {

	@Test
	public void testNewInstanceReplyStringInt01() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				"12.216.103.24", 
				0);
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testNewInstanceReplyStringInt02() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.GENERAL_SOCKS_SERVER_FAILURE, 
				"google.com", 
				1234);
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testNewInstanceReplyStringInt03() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.COMMAND_NOT_SUPPORTED, 
				"abcd:1234:ef56:abcd:789e:f123:456a:b789", 
				0xffff);
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

}
