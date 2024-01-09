package com.github.jh3nd3rs0n.jargyle.server;

import org.junit.Assert;
import org.junit.Test;

public class AddressRangeTest {

	@Test
	public void testContains01() {
		Assert.assertTrue(AddressRange.newInstanceOf("0.0.0.0").contains("0.0.0.0"));
	}

	@Test
	public void testContains02() {
		Assert.assertTrue(AddressRange.newInstanceOf("0.0.0.0").contains("0.0.0"));
	}

	@Test
	public void testContains03() {
		Assert.assertTrue(AddressRange.newInstanceOf("0.0.0.0").contains("0.0"));
	}

	@Test
	public void testContains04() {
		Assert.assertTrue(AddressRange.newInstanceOf("0.0.0.0").contains("0"));
	}

	@Test
	public void testContains05() {
		Assert.assertTrue(AddressRange.newInstanceOf("126.255.255.2-127.0.0.5").contains("127.0.0.1"));
	}

	@Test
	public void testContains06() {
		Assert.assertTrue(AddressRange.newInstanceOf("::-::2").contains("::1"));
	}

	@Test
	public void testContains07() {
		Assert.assertTrue(AddressRange.newInstanceOf("google.com").contains("google.com"));
	}

	@Test
	public void testContains08() {
		Assert.assertTrue(AddressRange.newInstanceOf("regex:.*google\\.com\\z").contains("google.com"));
	}

	@Test
	public void testContains09() {
		Assert.assertTrue(AddressRange.newInstanceOf("regex:.*google\\.com\\z").contains("anothergoogle.com"));
	}

}
