package com.github.jh3nd3rs0n.jargyle.transport.test.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

public class Socks5ReplyTest {

	@Test
	public void testNewInstanceReplyStringInt01() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				Address.newInstance("12.216.103.24"), 
				Port.newInstance(0));
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testNewInstanceReplyStringInt02() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.GENERAL_SOCKS_SERVER_FAILURE, 
				Address.newInstance("google.com"), 
				Port.newInstance(1234));
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testNewInstanceReplyStringInt03() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.COMMAND_NOT_SUPPORTED, 
				Address.newInstance("abcd:1234:ef56:abcd:789e:f123:456a:b789"), 
				Port.newInstance(0xffff));
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

}
