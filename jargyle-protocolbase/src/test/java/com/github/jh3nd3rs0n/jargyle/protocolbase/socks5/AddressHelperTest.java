package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AddressHelperTest {

	@Test
	public void testToByteArrayAddress01() {
		Address address1 = Address.newInstance("localhost");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress02() {
		Address address1 = Address.newInstance("127.0.0.1");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress03() {
		Address address1 = Address.newInstance("0:0:0:0:0:0:0:1");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress04() {
		Address address1 = Address.newInstance("github.com");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress05() {
		Address address1 = Address.newInstance("google.com");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress06() {
		Address address1 = Address.newInstance("news.google.com");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress07() {
		Address address1 = Address.newInstance("0.0.0.0");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress08() {
		Address address1 = Address.newInstance("0:0:0:0:0:0:0:0");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress09() {
		Address address1 = Address.newInstance("255.255.255.255");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress10() {
		Address address1 = Address.newInstance(
				"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress11() {
		Address address1 = Address.newInstance("255.200.100.0");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testToByteArrayAddress12() {
		Address address1 = Address.newInstance("1234:ab:cd:ef:fe:dc:ba:6789");
		Address address2 = AddressHelper.toAddress(AddressHelper.toByteArray(address1));
		Assert.assertEquals(address1, address2);
	}

}
