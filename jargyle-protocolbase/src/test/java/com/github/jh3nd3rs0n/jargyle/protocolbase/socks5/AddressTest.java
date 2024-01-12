package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AddressTest {

	@Test
	public void testNewInstanceFromString01() throws IOException {
		Address address1 = Address.newInstance("localhost");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString02() throws IOException {
		Address address1 = Address.newInstance("127.0.0.1");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString03() throws IOException {
		Address address1 = Address.newInstance("0:0:0:0:0:0:0:1");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString04() throws IOException {
		Address address1 = Address.newInstance("github.com");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString05() throws IOException {
		Address address1 = Address.newInstance("google.com");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString06() throws IOException {
		Address address1 = Address.newInstance("news.google.com");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString07() throws IOException {
		Address address1 = Address.newInstance("0.0.0.0");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString08() throws IOException {
		Address address1 = Address.newInstance("0:0:0:0:0:0:0:0");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString09() throws IOException {
		Address address1 = Address.newInstance("255.255.255.255");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString10() throws IOException {
		Address address1 = Address.newInstance(
				"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString11() throws IOException {
		Address address1 = Address.newInstance("255.200.100.0");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

	@Test
	public void testNewInstanceFromString12() throws IOException {
		Address address1 = Address.newInstance("1234:ab:cd:ef:fe:dc:ba:6789");
		Address address2 = Address.newInstance(address1.toByteArray());
		Assert.assertEquals(address1, address2);
	}

}
