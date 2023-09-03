package com.github.jh3nd3rs0n.jargyle.transport.test.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.AddressType;

public class AddressTypeTest {
	
	@Test
	public void testValueForString01() {
		assertEquals(AddressType.DOMAINNAME, AddressType.valueForString(
				"localhost"));
	}
	
	@Test
	public void testValueForString02() {
		assertEquals(AddressType.IPV4, AddressType.valueForString("127.0.0.1"));
	}

	@Test
	public void testValueForString03() {
		assertEquals(AddressType.IPV6, AddressType.valueForString(
				"0:0:0:0:0:0:0:1"));
	}
	
	@Test
	public void testValueForString04() {
		assertEquals(AddressType.DOMAINNAME, AddressType.valueForString(
				"github.com"));
	}
	
	@Test
	public void testValueForString05() {
		assertEquals(AddressType.DOMAINNAME, AddressType.valueForString(
				"google.com"));
	}
	
	@Test
	public void testValueForString06() {
		assertEquals(AddressType.DOMAINNAME, AddressType.valueForString(
				"news.google.com"));
	}
	
	@Test
	public void testValueForString07() {
		assertEquals(AddressType.IPV4, AddressType.valueForString("0.0.0.0"));
	}
	
	@Test
	public void testValueForString08() {
		assertEquals(AddressType.IPV6, AddressType.valueForString(
				"0:0:0:0:0:0:0:0"));
	}
	
	@Test
	public void testValueForString09() {
		assertEquals(AddressType.IPV4, AddressType.valueForString(
				"255.255.255.255"));
	}
	
	@Test
	public void testValueForString10() {
		assertEquals(AddressType.IPV6, AddressType.valueForString(
				"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
	}
	
	@Test
	public void testValueForString11() {
		assertEquals(AddressType.IPV4, AddressType.valueForString(
				"255.200.100.0"));
	}
	
	@Test
	public void testValueForString12() {
		assertEquals(AddressType.IPV6, AddressType.valueForString(
				"1234:ab:cd:ef:fe:dc:ba:6789"));
	}

	@Test
	public void testIsValueForString13() {
		assertEquals(AddressType.IPV4, AddressType.valueForString("0"));
	}
	
	@Test
	public void testIsValueForString14() {
		assertEquals(AddressType.IPV4, AddressType.valueForString(
				"4294967295"));
	}
	
	@Test
	public void testIsValueForString15() {
		assertEquals(AddressType.IPV4, AddressType.valueForString("0.0"));
	}
	
	@Test
	public void testIsValueForString16() {
		assertEquals(AddressType.IPV4, AddressType.valueForString(
				"255.16777215"));
	}
	
	@Test
	public void testIsValueForString17() {
		assertEquals(AddressType.IPV4, AddressType.valueForString("0.0.0"));
	}
	
	@Test
	public void testIsValueForString18() {
		assertEquals(AddressType.IPV4, AddressType.valueForString(
				"255.255.65535"));
	}
	
	@Test
	public void testIsValueForString19() {
		assertEquals(AddressType.IPV4, AddressType.valueForString("0.0.0.0"));
	}
	
	@Test
	public void testIsValueForString20() {
		assertEquals(AddressType.IPV4, AddressType.valueForString(
				"255.255.255.255"));
	}
	
	@Test
	public void testIsValueForString21() {
		assertEquals(AddressType.IPV6, AddressType.valueForString("::"));
	}
	
	@Test
	public void testIsValueForString22() {
		assertEquals(AddressType.IPV6, AddressType.valueForString(
				"FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"));
	}
	
	@Test
	public void testIsValueForString23() {
		assertEquals(AddressType.IPV6, AddressType.valueForString(
				"1111::1111"));
	}
	
	@Test
	public void testIsValueForString24() {
		assertEquals(AddressType.IPV6, AddressType.valueForString("::2222"));
	}
	
	@Test
	public void testIsValueForString25() {
		assertEquals(AddressType.IPV6, AddressType.valueForString("3333::"));
	}
	
}
