package com.github.jh3nd3rs0n.jargyle.protocolbase.test.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.UdpRequestHeader;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;

public class UdpRequestHeaderTest {

	@Test
	public void testNewInstanceIntStringIntByteArray01() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				UnsignedByte.newInstance(0), 
				Address.newInstance("12.216.103.24"), 
				Port.newInstance(0), 
				TestStringConstants.STRING_01.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testNewInstanceIntStringIntByteArray02() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				UnsignedByte.newInstance(1), 
				Address.newInstance("google.com"), 
				Port.newInstance(1234), 
				TestStringConstants.STRING_02.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testNewInstanceIntStringIntByteArray03() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				UnsignedByte.newInstance(255), 
				Address.newInstance("abcd:1234:ef56:abcd:789e:f123:456a:b789"), 
				Port.newInstance(0xffff), 
				TestStringConstants.STRING_03.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstance(udpDatagram1.toByteArray());
		assertEquals(udpDatagram1, udpDatagram2);
	}

}
