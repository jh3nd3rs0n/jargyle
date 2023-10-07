package com.github.jh3nd3rs0n.jargyle.transport.test.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public class Socks5RequestTest {

	@Test
	public void testNewInstanceCommandStringInt01() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.CONNECT, 
				Address.newInstance("12.216.103.24"), 
				Port.newInstance(0));
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceCommandStringInt02() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.BIND, 
				Address.newInstance("google.com"), 
				Port.newInstance(1234));
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceCommandStringInt03() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.UDP_ASSOCIATE, 
				Address.newInstance("abcd:1234:ef56:abcd:789e:f123:456a:b789"), 
				Port.newInstance(0xffff));
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

}
