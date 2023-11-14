package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressHelper;

public enum AddressType {

	IPV4((byte) 0x01) {
		
		@Override
		public boolean isValueForString(final String string) {
			return InetAddressHelper.isIpv4Address(string);
		}

		@Override
		public Address newAddress(final String string) {
			if (!this.isValueForString(string)) {
				throw new IllegalArgumentException(String.format(
						"invalid address: %s", string));
			}
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(string);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(UnsignedByte.newInstance(this.byteValue()).intValue());
			try {
				out.write(inetAddress.getAddress());
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			return new Address(this, out.toByteArray(), string);
		}
		
	},
	
	DOMAINNAME((byte) 0x03) {
		
		@Override
		public boolean isValueForString(final String string) {
			return InetAddressHelper.isDomainname(string);
		}

		@Override
		public Address newAddress(final String string) {
			if (!this.isValueForString(string)) {
				throw new IllegalArgumentException(String.format(
						"invalid address: %s", string));
			}
			byte[] bytes = string.getBytes();
			if (bytes.length > UnsignedByte.MAX_INT_VALUE) {
				throw new IllegalArgumentException(String.format(
						"expected address length less than or equal to %s. "
						+ "actual address length is %s",
						UnsignedByte.MAX_INT_VALUE,
						bytes.length));
			}
			byte[] b = new byte[bytes.length + 1];
			b[0] = (byte) bytes.length;
			for (int i = 1; i < b.length; i++) {
				b[i] = bytes[i - 1];
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(UnsignedByte.newInstance(this.byteValue()).intValue());
			try {
				out.write(b);
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			return new Address(this, out.toByteArray(), string);
		}
		
	},
	
	IPV6((byte) 0x04) {
		
		@Override
		public boolean isValueForString(final String string) {
			return InetAddressHelper.isIpv6Address(string);
		}

		@Override
		public Address newAddress(final String string) {
			if (!this.isValueForString(string)) {
				throw new IllegalArgumentException(String.format(
						"invalid address: %s", string));
			}
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(string);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(UnsignedByte.newInstance(this.byteValue()).intValue());
			try {
				out.write(inetAddress.getAddress());
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			return new Address(this, out.toByteArray(), string);
		}
		
	};
	
	public static AddressType valueForString(final String string) {
		for (AddressType addressType : AddressType.values()) {
			if (!addressType.equals(DOMAINNAME) 
					&& addressType.isValueForString(string)) {
				return addressType;
			}
		}
		if (DOMAINNAME.isValueForString(string)) {
			return DOMAINNAME;
		}
		throw new IllegalArgumentException(String.format(
				"unable to determine address type for the specified address: %s",
				string));
	}
	
	public static AddressType valueOfByte(final byte b) {
		for (AddressType addressType : AddressType.values()) {
			if (addressType.byteValue() == b) {
				return addressType;
			}
		}
		String str = Arrays.stream(AddressType.values())
				.map(AddressType::byteValue)
				.map(bv -> UnsignedByte.newInstance(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected address type must be one of the following values: "
				+ "%s. actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.newInstance(b).intValue())));
	}
	
	private final byte byteValue;
	
	private AddressType(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
	public abstract boolean isValueForString(final String string);
	
	public abstract Address newAddress(final String string);
}
