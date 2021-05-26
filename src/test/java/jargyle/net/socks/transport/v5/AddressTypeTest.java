package jargyle.net.socks.transport.v5;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class AddressTypeTest {

	@Test
	public void testNewAddress01() throws IOException {
		Address address1 = AddressType.DOMAINNAME.newAddress("localhost");
		Address address2 = AddressType.DOMAINNAME.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress02() throws IOException {
		Address address1 = AddressType.IPV4.newAddress("127.0.0.1");
		Address address2 = AddressType.IPV4.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress03() throws IOException {
		Address address1 = AddressType.IPV6.newAddress("0:0:0:0:0:0:0:1");
		Address address2 = AddressType.IPV6.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress04() throws IOException {
		Address address1 = AddressType.DOMAINNAME.newAddress("github.com");
		Address address2 = AddressType.DOMAINNAME.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress05() throws IOException {
		Address address1 = AddressType.DOMAINNAME.newAddress("google.com");
		Address address2 = AddressType.DOMAINNAME.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress06() throws IOException {
		Address address1 = AddressType.DOMAINNAME.newAddress("news.google.com");
		Address address2 = AddressType.DOMAINNAME.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress07() throws IOException {
		Address address1 = AddressType.IPV4.newAddress("0.0.0.0");
		Address address2 = AddressType.IPV4.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress08() throws IOException {
		Address address1 = AddressType.IPV6.newAddress("0:0:0:0:0:0:0:0");
		Address address2 = AddressType.IPV6.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress09() throws IOException {
		Address address1 = AddressType.IPV4.newAddress("255.255.255.255");
		Address address2 = AddressType.IPV4.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress10() throws IOException {
		Address address1 = AddressType.IPV6.newAddress(
				"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		Address address2 = AddressType.IPV6.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress11() throws IOException {
		Address address1 = AddressType.IPV4.newAddress("255.200.100.0");
		Address address2 = AddressType.IPV4.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}

	@Test
	public void testNewAddress12() throws IOException {
		Address address1 = AddressType.IPV6.newAddress(
				"1234:ab:cd:ef:fe:dc:ba:6789");
		Address address2 = AddressType.IPV6.newAddressFrom(
				new ByteArrayInputStream(address1.toByteArray()));
		assertEquals(address1, address2);
	}
	
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
	
}
