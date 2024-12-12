package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import org.junit.Assert;
import org.junit.Test;

public class UdpRequestTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceFromByteArrayForIllegalArgumentException01() {
		UdpRequest.newInstanceFrom(new byte[] {
				(byte) 0x00,
				(byte) 0xff, // wrong value for reserved field
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x7f,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x02,
				(byte) 0x03 });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceFromByteArrayForIllegalArgumentException02() {
		UdpRequest.newInstanceFrom(new byte[] {});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceFromByteArrayForIllegalArgumentException03() {
		UdpRequest.newInstanceFrom(new byte[] { (byte) 0x00 });
	}

	@Test
	public void testEqualsObject01() {
		UdpRequest udpRequest = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertEquals(udpRequest, udpRequest);
	}

	@Test
	public void testEqualsObject02() {
		UdpRequest udpRequest = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertNotEquals(udpRequest, null);
	}

	@Test
	public void testEqualsObject03() {
		Object obj1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Object obj2 = new Object();
		Assert.assertNotEquals(obj1, obj2);
	}

	@Test
	public void testEqualsObject04() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(255),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertNotEquals(udpRequest1, udpRequest2);
	}

	@Test
	public void testEqualsObject05() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("::1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertNotEquals(udpRequest1, udpRequest2);
	}

	@Test
	public void testEqualsObject06() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(65535),
				StringConstants.STRING_02.getBytes());
		Assert.assertNotEquals(udpRequest1, udpRequest2);
	}

	@Test
	public void testEqualsObject07() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_03.getBytes());
		Assert.assertNotEquals(udpRequest1, udpRequest2);
	}

	@Test
	public void testEqualsObject08() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertEquals(udpRequest1, udpRequest2);
	}

	@Test
	public void testHashCode01() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(255),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertNotEquals(udpRequest1.hashCode(), udpRequest2.hashCode());
	}

	@Test
	public void testHashCode02() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("::1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertNotEquals(udpRequest1.hashCode(), udpRequest2.hashCode());
	}

	@Test
	public void testHashCode03() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(65535),
				StringConstants.STRING_02.getBytes());
		Assert.assertNotEquals(udpRequest1.hashCode(), udpRequest2.hashCode());
	}

	@Test
	public void testHashCode04() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_03.getBytes());
		Assert.assertNotEquals(udpRequest1.hashCode(), udpRequest2.hashCode());
	}

	@Test
	public void testHashCode05() {
		UdpRequest udpRequest1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpRequest2 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		Assert.assertEquals(udpRequest1.hashCode(), udpRequest2.hashCode());
	}

	@Test
	public void testToByteArray01() {
		UdpRequest udpDatagram1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(0),
				Address.newInstanceFrom("12.216.103.24"),
				Port.valueOf(0),
				StringConstants.STRING_02.getBytes());
		UdpRequest udpDatagram2 = UdpRequest.newInstanceFrom(
				udpDatagram1.toByteArray());
		Assert.assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testToByteArray02() {
		UdpRequest udpDatagram1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(1),
				Address.newInstanceFrom("google.com"),
				Port.valueOf(1234),
				StringConstants.STRING_03.getBytes());
		UdpRequest udpDatagram2 = UdpRequest.newInstanceFrom(
				udpDatagram1.toByteArray());
		Assert.assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testToByteArray03() {
		UdpRequest udpDatagram1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(123),
				Address.newInstanceFrom("4256:1234:ef56:dcba:789e:f123:456a:abcd"),
				Port.valueOf(0x9999),
				StringConstants.STRING_03.getBytes());
		UdpRequest udpDatagram2 = UdpRequest.newInstanceFrom(
				udpDatagram1.toByteArray());
		Assert.assertEquals(udpDatagram1, udpDatagram2);
	}

	@Test
	public void testToByteArray04() {
		UdpRequest udpDatagram1 = UdpRequest.newInstance(
				UnsignedByte.valueOf(255),
				Address.newInstanceFrom("abcd:1234:ef56:abcd:789e:f123:456a:b789"),
				Port.valueOf(0xffff),
				StringConstants.STRING_04.getBytes());
		UdpRequest udpDatagram2 = UdpRequest.newInstanceFrom(
				udpDatagram1.toByteArray());
		Assert.assertEquals(udpDatagram1, udpDatagram2);
	}

}
