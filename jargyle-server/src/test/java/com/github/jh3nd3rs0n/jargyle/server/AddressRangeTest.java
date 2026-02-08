package com.github.jh3nd3rs0n.jargyle.server;

import org.junit.Assert;
import org.junit.Test;

public class AddressRangeTest {

	@Test
	public void testContains01() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").covers("0.0.0.0"));
	}

	@Test
	public void testContains02() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").covers("0.0.0"));
	}

	@Test
	public void testContains03() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").covers("0.0"));
	}

	@Test
	public void testContains04() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").covers("0"));
	}

	@Test
	public void testContains05() {
		Assert.assertTrue(AddressRange.newInstanceFrom("126.255.255.2-127.0.0.5").covers("127.0.0.1"));
	}

	@Test
	public void testContains06() {
		Assert.assertTrue(AddressRange.newInstanceFrom("::-::2").covers("::1"));
	}

	@Test
	public void testContains07() {
		Assert.assertTrue(AddressRange.newInstanceFrom("example.com").covers("example.com"));
	}

	@Test
	public void testContains08() {
		Assert.assertTrue(AddressRange.newInstanceFrom("regex:.*example\\.com\\z").covers("example.com"));
	}

	@Test
	public void testContains09() {
		Assert.assertTrue(AddressRange.newInstanceFrom("regex:.*example\\.com\\z").covers("anotherexample.com"));
	}

}
