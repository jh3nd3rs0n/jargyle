package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

public class Socks5ReplyTest {

	@Test
	public void testToByteArray01() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				Address.newInstance("12.216.103.24"), 
				Port.valueOf(0));
		Socks5Reply socks5Reply2 = Socks5Reply.newInstanceFrom(
				socks5Reply1.toByteArray());
		Assert.assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testToByteArray02() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.GENERAL_SOCKS_SERVER_FAILURE, 
				Address.newInstance("google.com"), 
				Port.valueOf(1234));
		Socks5Reply socks5Reply2 = Socks5Reply.newInstanceFrom(
				socks5Reply1.toByteArray());
		Assert.assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testToByteArray03() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.COMMAND_NOT_SUPPORTED, 
				Address.newInstance("abcd:1234:ef56:abcd:789e:f123:456a:b789"), 
				Port.valueOf(0xffff));
		Socks5Reply socks5Reply2 = Socks5Reply.newInstanceFrom(
				socks5Reply1.toByteArray());
		Assert.assertEquals(socks5Reply1, socks5Reply2);
	}

}
