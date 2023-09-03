package com.github.jh3nd3rs0n.jargyle.transport.test.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.UdpRequestHeader;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;

public class UdpRequestHeaderTest {

	@Test
	public void testNewInstanceIntStringIntByteArray01() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				0, 
				"12.216.103.24", 
				0, 
				TestStringConstants.STRING_01.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testNewInstanceIntStringIntByteArray02() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				1, 
				"google.com", 
				1234, 
				TestStringConstants.STRING_02.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testNewInstanceIntStringIntByteArray03() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				255, 
				"abcd:1234:ef56:abcd:789e:f123:456a:b789", 
				0xffff, 
				TestStringConstants.STRING_03.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

}
