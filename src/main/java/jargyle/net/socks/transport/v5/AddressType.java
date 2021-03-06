package jargyle.net.socks.transport.v5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.util.UnsignedByte;

public enum AddressType {

	IPV4((byte) 0x01) {
		
		private static final int ADDRESS_LENGTH = 4;
		private static final String ADDRESS_REGEX = 
				"^[\\d]{1,3}(\\.[\\d]{1,3}){0,3}$";
		
		@Override
		public boolean isValueForString(final String string) {
			return string.matches(ADDRESS_REGEX);
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
				throw new AssertionError(e);
			}
			return new Address(this, inetAddress.getAddress(), string);
		}

		@Override
		public Address newAddressFrom(final InputStream in) throws IOException {
			byte[] bytes = new byte[ADDRESS_LENGTH];
			int bytesRead = in.read(bytes);
			if (bytesRead != ADDRESS_LENGTH) { 
				throw new IOException(String.format(
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
				throw new IOException(String.format(
						"expected address length is %s. "
						+ "actual address length is %s", 
						ADDRESS_LENGTH, 
						bytesRead));
			}
			if (!(inetAddress instanceof Inet4Address)) {
				throw new IOException(String.format(
						"raw IP address (%s) not IPv4", inetAddress));
			}
			return new Address(this, bytes, inetAddress.getHostAddress());
		}
		
	},
	
	DOMAINNAME((byte) 0x03) {
		
		private static final String ADDRESS_REGEX = 
				"^([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9])(\\.([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9]))*$";
		
		@Override
		public boolean isValueForString(final String string) {
			return string.matches(ADDRESS_REGEX);
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
			return new Address(this, b, string);
		}

		@Override
		public Address newAddressFrom(final InputStream in) throws IOException {
			UnsignedByte octetCount = UnsignedByte.newInstanceFrom(in);
			byte[] bytes = new byte[octetCount.intValue()];
			int bytesRead = in.read(bytes);
			if (octetCount.intValue() != bytesRead) {
				throw new IOException(String.format(
						"expected address length is %s. "
						+ "actual address length is %s", 
						octetCount.intValue(), 
						bytesRead));
			}
			bytes = Arrays.copyOf(bytes, bytes.length);
			String string = new String(bytes);
			if (!this.isValueForString(string)) {
				throw new IOException(String.format(
						"invalid address: %s", string));
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(UnsignedByte.newInstance(
					octetCount.byteValue()).intValue());
			out.write(bytes);
			return new Address(this, out.toByteArray(), string);
		}
		
	},
	
	IPV6((byte) 0x04) {
		
		private static final int ADDRESS_LENGTH = 16;
		private static final String ADDRESS_REGEX = 
				"^[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}$";
		
		@Override
		public boolean isValueForString(final String string) {
			return string.matches(ADDRESS_REGEX);
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
				throw new AssertionError(e);
			}
			return new Address(this, inetAddress.getAddress(), string);
		}

		@Override
		public Address newAddressFrom(final InputStream in) throws IOException {
			byte[] bytes = new byte[ADDRESS_LENGTH];
			int bytesRead = in.read(bytes);
			if (bytesRead != ADDRESS_LENGTH) { 
				throw new IOException(String.format(
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
				throw new IOException(String.format(
						"expected address length is %s. "
						+ "actual address length is %s", 
						ADDRESS_LENGTH, 
						bytesRead));
			}
			if (!(inetAddress instanceof Inet6Address)) {
				throw new IOException(String.format(
						"raw IP address (%s) not IPv6", inetAddress));
			}
			return new Address(this, bytes, inetAddress.getHostAddress());
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
		StringBuilder sb = new StringBuilder();
		List<AddressType> list = Arrays.asList(AddressType.values());
		for (Iterator<AddressType> iterator = list.iterator();
				iterator.hasNext();) {
			AddressType value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected address type must be one of the following "
						+ "values: %s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	public static AddressType valueOfByteFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByte.newInstanceFrom(in);
		AddressType addressType = null;
		try {
			addressType = valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		return addressType;
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
	
	public abstract Address newAddressFrom(
			final InputStream in) throws IOException;
}
