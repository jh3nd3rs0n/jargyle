package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import org.junit.Assert;
import org.junit.Test;

public class UdpRequestHeaderTest {

	@Test
	public void testToByteArray01() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstance("12.216.103.24"), 
				Port.valueOf(0),
				TestStringConstants.STRING_01.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstanceFrom(
				udpDatagram1.toByteArray());
		Assert.assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testToByteArray02() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				UnsignedByte.valueOf(1),
				Address.newInstance("google.com"), 
				Port.valueOf(1234),
				TestStringConstants.STRING_02.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstanceFrom(
				udpDatagram1.toByteArray());
		Assert.assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testToByteArray03() {
		UdpRequestHeader udpDatagram1 = UdpRequestHeader.newInstance(
				UnsignedByte.valueOf(255),
				Address.newInstance("abcd:1234:ef56:abcd:789e:f123:456a:b789"), 
				Port.valueOf(0xffff),
				TestStringConstants.STRING_03.getBytes());
		UdpRequestHeader udpDatagram2 = UdpRequestHeader.newInstanceFrom(
				udpDatagram1.toByteArray());
		Assert.assertEquals(udpDatagram1, udpDatagram2);
	}

}
