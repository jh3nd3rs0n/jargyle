package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

public class Socks5RequestTest {

	@Test
	public void testNewInstanceCommandAddressPort01() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.CONNECT, 
				Address.newInstance("12.216.103.24"), 
				Port.valueOf(0));
		Socks5Request socks5Request2 = Socks5Request.newInstance(
				socks5Request1.toByteArray());
		Assert.assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceCommandAddressPort02() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.BIND, 
				Address.newInstance("google.com"), 
				Port.valueOf(1234));
		Socks5Request socks5Request2 = Socks5Request.newInstance(
				socks5Request1.toByteArray());
		Assert.assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceCommandAddressPort03() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.UDP_ASSOCIATE, 
				Address.newInstance("abcd:1234:ef56:abcd:789e:f123:456a:b789"), 
				Port.valueOf(0xffff));
		Socks5Request socks5Request2 = Socks5Request.newInstance(
				socks5Request1.toByteArray());
		Assert.assertEquals(socks5Request1, socks5Request2);
	}

}
