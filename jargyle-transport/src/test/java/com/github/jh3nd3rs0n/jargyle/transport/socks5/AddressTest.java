package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;

public class AddressTest {

	@Test
	public void testNewInstance01() throws IOException {
		Address address1 = Address.newInstance("localhost");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance02() throws IOException {
		Address address1 = Address.newInstance("127.0.0.1");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance03() throws IOException {
		Address address1 = Address.newInstance("0:0:0:0:0:0:0:1");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance04() throws IOException {
		Address address1 = Address.newInstance("github.com");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance05() throws IOException {
		Address address1 = Address.newInstance("google.com");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance06() throws IOException {
		Address address1 = Address.newInstance("news.google.com");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance07() throws IOException {
		Address address1 = Address.newInstance("0.0.0.0");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance08() throws IOException {
		Address address1 = Address.newInstance("0:0:0:0:0:0:0:0");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance09() throws IOException {
		Address address1 = Address.newInstance("255.255.255.255");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance10() throws IOException {
		Address address1 = Address.newInstance(
				"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance11() throws IOException {
		Address address1 = Address.newInstance("255.200.100.0");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

	@Test
	public void testNewInstance12() throws IOException {
		Address address1 = Address.newInstance("1234:ab:cd:ef:fe:dc:ba:6789");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				address1.getAddressType().byteValue()).intValue());
		out.write(address1.toByteArray());
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		Address address2 = Address.newInstanceFrom(in);
		assertEquals(address1, address2);
	}

}
