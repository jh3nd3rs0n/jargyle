package com.github.jh3nd3rs0n.jargyle.server;

import org.junit.Assert;
import org.junit.Test;

public class AddressRangeTest {

	@Test
	public void testContains01() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").has("0.0.0.0"));
	}

	@Test
	public void testContains02() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").has("0.0.0"));
	}

	@Test
	public void testContains03() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").has("0.0"));
	}

	@Test
	public void testContains04() {
		Assert.assertTrue(AddressRange.newInstanceFrom("0.0.0.0").has("0"));
	}

	@Test
	public void testContains05() {
		Assert.assertTrue(AddressRange.newInstanceFrom("126.255.255.2-127.0.0.5").has("127.0.0.1"));
	}

	@Test
	public void testContains06() {
		Assert.assertTrue(AddressRange.newInstanceFrom("::-::2").has("::1"));
	}

	@Test
	public void testContains07() {
		Assert.assertTrue(AddressRange.newInstanceFrom("google.com").has("google.com"));
	}

	@Test
	public void testContains08() {
		Assert.assertTrue(AddressRange.newInstanceFrom("regex:.*google\\.com\\z").has("google.com"));
	}

	@Test
	public void testContains09() {
		Assert.assertTrue(AddressRange.newInstanceFrom("regex:.*google\\.com\\z").has("anothergoogle.com"));
	}

}
