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

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostName;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;

final class AddressInputHelper {
	
	public static Address readAddressFrom(
			final InputStream in) throws IOException {
		AddressType addressType = readAddressTypeFrom(in);
		Address address = null;
		switch (addressType) {
		case IPV4:
			address = readIpv4AddressFrom(in);
			break;
		case DOMAINNAME:
			address = readDomainnameAddressFrom(in);
			break;
		case IPV6:
			address = readIpv6AddressFrom(in);
			break;
		default:
			throw new AddressTypeNotSupportedException(UnsignedByte.valueOf(
					addressType.byteValue()));
		}
		return address;
	}
	
	private static AddressType readAddressTypeFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		AddressType addressType = null;
		try {
			addressType = AddressType.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new AddressTypeNotSupportedException(b);
		}
		return addressType;
	}
	
	private static Address readDomainnameAddressFrom(
			final InputStream in) throws IOException {
		UnsignedByte octetCount = UnsignedByteInputHelper.readUnsignedByteFrom(
				in);
		byte[] bytes = new byte[octetCount.intValue()];
		int bytesRead = in.read(bytes);
		if (octetCount.intValue() != bytesRead) {
			throw new EOFException(String.format(
					"expected address length is %s. "
					+ "actual address length is %s", 
					octetCount.intValue(), 
					bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytes.length);
		String string = new String(bytes);
		if (!(Host.newInstance(string) instanceof HostName)) {
			throw new Socks5Exception(String.format(
					"invalid address: %s", string));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.valueOf(
				AddressType.DOMAINNAME.byteValue()).intValue());
		out.write(UnsignedByte.valueOf(
				octetCount.byteValue()).intValue());
		out.write(bytes);
		return new Address(AddressType.DOMAINNAME, out.toByteArray(), string);		
	}
	
	private static Address readIpv4AddressFrom(
			final InputStream in) throws IOException {
		final int ADDRESS_LENGTH = 4;
		byte[] bytes = new byte[ADDRESS_LENGTH];
		int bytesRead = in.read(bytes);
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
		out.write(UnsignedByte.valueOf(
				AddressType.IPV4.byteValue()).intValue());
		out.write(bytes);
		return new Address(
				AddressType.IPV4, 
				out.toByteArray(), 
				inetAddress.getHostAddress());
	}
	
	private static Address readIpv6AddressFrom(
			final InputStream in) throws IOException {
		final int ADDRESS_LENGTH = 16;
		byte[] bytes = new byte[ADDRESS_LENGTH];
		int bytesRead = in.read(bytes);
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
		out.write(UnsignedByte.valueOf(
				AddressType.IPV6.byteValue()).intValue());
		out.write(bytes);
		return new Address(
				AddressType.IPV6, 
				out.toByteArray(), 
				inetAddress.getHostAddress());		
	}
	
	private AddressInputHelper() { }

}
