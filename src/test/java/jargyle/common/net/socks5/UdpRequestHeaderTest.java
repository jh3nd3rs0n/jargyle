package jargyle.common.net.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.UdpRequestHeader;

public class UdpRequestHeaderTest {

	@Test
	public void testNewInstanceIntAddressTypeStringIntByteArray01() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				0, 
				AddressType.IP_V4_ADDRESS, 
				"12.216.103.24", 
				0, 
				"Hello, World".getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testNewInstanceIntAddressTypeStringIntByteArray02() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				1, 
				AddressType.DOMAINNAME, 
				"google.com", 
				1234, 
				"Goodbye, Cruel World".getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testNewInstanceIntAddressTypeStringIntByteArray03() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				255, 
				AddressType.IP_V6_ADDRESS, 
				"abcd:1234:ef56:abcd:789e:f123:456a:b789", 
				0xffff, 
				"Ugh...".getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

}
