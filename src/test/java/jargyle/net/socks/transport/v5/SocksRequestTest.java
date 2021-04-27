package jargyle.net.socks.transport.v5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SocksRequestTest {

	@Test
	public void testNewInstanceCommandStringInt01() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.CONNECT, 
				"12.216.103.24", 
				0);
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceCommandStringInt02() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.BIND, 
				"google.com", 
				1234);
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceCommandStringInt03() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.UDP_ASSOCIATE, 
				"abcd:1234:ef56:abcd:789e:f123:456a:b789", 
				0xffff);
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

}
