package jargyle.common.net.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SocksRequestTest {

	@Test
	public void testNewInstanceVersionCommandAddressTypeStringInt01() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.CONNECT, 
				AddressType.IP_V4_ADDRESS, 
				"12.216.103.24", 
				0);
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceVersionCommandAddressTypeStringInt02() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.BIND, 
				AddressType.DOMAINNAME, 
				"google.com", 
				1234);
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

	@Test
	public void testNewInstanceVersionCommandAddressTypeStringInt03() {
		Socks5Request socks5Request1 = Socks5Request.newInstance(
				Command.UDP_ASSOCIATE, 
				AddressType.IP_V6_ADDRESS, 
				"abcd:1234:ef56:abcd:789e:f123:456a:b789", 
				0xffff);
		Socks5Request socks5Request2 = Socks5Request.newInstance(socks5Request1.toByteArray());
		assertEquals(socks5Request1, socks5Request2);
	}

}
