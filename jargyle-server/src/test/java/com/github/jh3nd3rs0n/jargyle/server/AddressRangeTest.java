package com.github.jh3nd3rs0n.jargyle.server;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressRangeTest {

	@Test
	public void testContains01() {
		assertTrue(AddressRange.newInstance("0.0.0.0").contains("0.0.0.0"));
	}

	@Test
	public void testContains02() {
		assertTrue(AddressRange.newInstance("0.0.0.0").contains("0.0.0"));
	}

	@Test
	public void testContains03() {
		assertTrue(AddressRange.newInstance("0.0.0.0").contains("0.0"));
	}

	@Test
	public void testContains04() {
		assertTrue(AddressRange.newInstance("0.0.0.0").contains("0"));
	}

	@Test
	public void testContains05() {
		assertTrue(AddressRange.newInstance("126.255.255.2-127.0.0.5").contains("127.0.0.1"));
	}

	@Test
	public void testContains06() {
		assertTrue(AddressRange.newInstance("::-::2").contains("::1"));
	}

	@Test
	public void testContains07() {
		assertTrue(AddressRange.newInstance("google.com").contains("google.com"));
	}

	@Test
	public void testContains08() {
		assertTrue(AddressRange.newInstance("regex:.*google.com\\z").contains("google.com"));
	}

	@Test
	public void testContains09() {
		assertTrue(AddressRange.newInstance("regex:.*google.com\\z").contains("anothergoogle.com"));
	}

}
