package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

final class AddressInputStream extends Socks5InputStream {

	public AddressInputStream(final InputStream in) {
		super(in);
	}
	
	public Address readAddress() throws IOException {
		AddressType addressType = this.readAddressType();
		Address address = null;
		switch (addressType) {
		case IPV4:
			address = this.readIpv4Address();
			break;
		case DOMAINNAME:
			address = this.readDomainnameAddress();
			break;
		case IPV6:
			address = this.readIpv6Address();
			break;
		default:
			throw new AddressTypeNotSupportedException(UnsignedByte.newInstance(
					addressType.byteValue()));
		}
		return address;
	}
	
	private AddressType readAddressType() throws IOException {
		UnsignedByte b = this.readUnsignedByte();
		AddressType addressType = null;
		try {
			addressType = AddressType.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new AddressTypeNotSupportedException(b);
		}
		return addressType;
	}
	
	private Address readDomainnameAddress() throws IOException {
		UnsignedByte octetCount = this.readUnsignedByte();
		byte[] bytes = new byte[octetCount.intValue()];
		int bytesRead = this.in.read(bytes);
		if (octetCount.intValue() != bytesRead) {
			throw new EOFException(String.format(
					"expected address length is %s. "
					+ "actual address length is %s", 
					octetCount.intValue(), 
					bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytes.length);
		String string = new String(bytes);
		if (!AddressType.DOMAINNAME.isValueForString(string)) {
			throw new Socks5Exception(String.format(
					"invalid address: %s", string));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				AddressType.DOMAINNAME.byteValue()).intValue());
		out.write(UnsignedByte.newInstance(
				octetCount.byteValue()).intValue());
		out.write(bytes);
		return new Address(AddressType.DOMAINNAME, out.toByteArray(), string);		
	}
	
	private Address readIpv4Address() throws IOException {
		final int ADDRESS_LENGTH = 4;
		byte[] bytes = new byte[ADDRESS_LENGTH];
		int bytesRead = this.in.read(bytes);
		if (bytesRead != ADDRESS_LENGTH) { 
			throw new EOFException(String.format(
					"expected address length is %s. "
					+ "actual address length is %s", 
					ADDRESS_LENGTH, 
					bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByAddress(bytes);
		} catch (UnknownHostException e) {
			throw new Socks5Exception(String.format(
					"expected address length is %s. "
					+ "actual address length is %s", 
					ADDRESS_LENGTH, 
					bytesRead));
		}
		if (!(inetAddress instanceof Inet4Address)) {
			throw new Socks5Exception(String.format(
					"raw IP address (%s) not IPv4", inetAddress));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				AddressType.IPV4.byteValue()).intValue());
		out.write(bytes);
		return new Address(
				AddressType.IPV4, 
				out.toByteArray(), 
				inetAddress.getHostAddress());
	}
	
	private Address readIpv6Address() throws IOException {
		final int ADDRESS_LENGTH = 16;
		byte[] bytes = new byte[ADDRESS_LENGTH];
		int bytesRead = this.in.read(bytes);
		if (bytesRead != ADDRESS_LENGTH) { 
			throw new EOFException(String.format(
					"expected address length is %s. "
					+ "actual address length is %s", 
					ADDRESS_LENGTH, 
					bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytes.length);
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByAddress(bytes);
		} catch (UnknownHostException e) {
			throw new Socks5Exception(String.format(
					"expected address length is %s. "
					+ "actual address length is %s", 
					ADDRESS_LENGTH, 
					bytesRead));
		}
		if (!(inetAddress instanceof Inet6Address)) {
			throw new Socks5Exception(String.format(
					"raw IP address (%s) not IPv6", inetAddress));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.newInstance(
				AddressType.IPV6.byteValue()).intValue());
		out.write(bytes);
		return new Address(
				AddressType.IPV6, 
				out.toByteArray(), 
				inetAddress.getHostAddress());		
	}

}
