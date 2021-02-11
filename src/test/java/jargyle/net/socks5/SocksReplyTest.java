package jargyle.net.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SocksReplyTest {

	@Test
	public void testNewInstanceVersionReplyAddressTypeStringInt01() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				AddressType.IP_V4_ADDRESS, 
				"12.216.103.24", 
				0);
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testNewInstanceVersionReplyAddressTypeStringInt02() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.GENERAL_SOCKS_SERVER_FAILURE, 
				AddressType.DOMAINNAME, 
				"google.com", 
				1234);
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

	@Test
	public void testNewInstanceVersionReplyAddressTypeStringInt03() {
		Socks5Reply socks5Reply1 = Socks5Reply.newInstance(
				Reply.COMMAND_NOT_SUPPORTED, 
				AddressType.IP_V6_ADDRESS, 
				"abcd:1234:ef56:abcd:789e:f123:456a:b789", 
				0xffff);
		Socks5Reply socks5Reply2 = Socks5Reply.newInstance(socks5Reply1.toByteArray());
		assertEquals(socks5Reply1, socks5Reply2);
	}

}
