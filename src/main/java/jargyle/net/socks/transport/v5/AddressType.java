package jargyle.net.socks.transport.v5;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.util.UnsignedByte;

public enum AddressType {

	IP_V4_ADDRESS((byte) 0x01) {
		
		private static final int ADDRESS_LENGTH = 4;
		private static final String ADDRESS_REGEX = 
				"^[\\d]{1,3}(\\.[\\d]{1,3}){0,3}$";
		
		@Override
		public int getAddressLength(final byte firstByte) {
			return ADDRESS_LENGTH;
		}

		@Override
		public boolean isAddress(final String s) {
			return s.matches(ADDRESS_REGEX);
		}

		@Override
		public String readAddress(final byte[] b) {
			InetAddress address = null;
			try {
				address = InetAddress.getByAddress(b);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(String.format(
						"expected address length is %s. "
						+ "actual address length is %s", 
						ADDRESS_LENGTH, 
						b.length));
			}
			if (!(address instanceof Inet4Address)) {
				throw new IllegalArgumentException(String.format(
						"raw IP address (%s) not IPv4", address));
			}
			return address.getHostAddress();
		}

		@Override
		public byte[] writeAddress(final String address) {
			if (!this.isAddress(address)) {
				throw new IllegalArgumentException(String.format(
						"invalid address: %s", address));
			}
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(address);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return inetAddress.getAddress();
		}
	},
	
	DOMAINNAME((byte) 0x03) {
		
		private static final String ADDRESS_REGEX = 
				"^([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9])(\\.([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9]))*$";
		
		@Override
		public int getAddressLength(final byte firstByte) {
			if (firstByte < 0x01) {
				throw new IllegalArgumentException(
						"domain name address length must be at 1");
			}
			// length of the byte of the address length + the address length 
			return 1 + UnsignedByte.newInstance(firstByte).intValue();
		}

		@Override
		public boolean isAddress(final String s) {
			return s.matches(ADDRESS_REGEX);
		}

		@Override
		public String readAddress(final byte[] b) {
			if (b.length <= 1) {
				throw new IllegalArgumentException(
						"expected address length greater than 1. "
						+ "actual address length is " + b.length);
			}
			int octetCount = UnsignedByte.newInstance(b[0]).intValue();
			if (octetCount != b.length - 1) {
				throw new IllegalArgumentException(String.format(
						"expected address length is %s. "
						+ "actual address length is %s", 
						octetCount + 1, 
						b.length));
			}
			String address = new String(Arrays.copyOfRange(b, 1, b.length));
			if (!this.isAddress(address)) {
				throw new IllegalArgumentException(String.format(
						"invalid address: %s", address));
			}
			return address;
		}

		@Override
		public byte[] writeAddress(final String address) {
			if (!this.isAddress(address)) {
				throw new IllegalArgumentException(String.format(
						"invalid address: %s", address));
			}
			byte[] bytes = address.getBytes();
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
			return b;
		}
	},
	
	IP_V6_ADDRESS((byte) 0x04) {
		
		private static final int ADDRESS_LENGTH = 16;
		private static final String ADDRESS_REGEX = 
				"^[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}$";
		
		@Override
		public int getAddressLength(final byte firstByte) {
			return ADDRESS_LENGTH;
		}

		@Override
		public boolean isAddress(final String s) {
			return s.matches(ADDRESS_REGEX);
		}

		@Override
		public String readAddress(final byte[] b) {
			InetAddress address = null;
			try {
				address = InetAddress.getByAddress(b);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(String.format(
						"expected address length is %s. "
						+ "actual address length is %s", 
						ADDRESS_LENGTH, 
						b.length));
			}
			if (!(address instanceof Inet6Address)) {
				throw new IllegalArgumentException(String.format(
						"raw IP address (%s) not IPv6", address));
			}
			return address.getHostAddress();
		}

		@Override
		public byte[] writeAddress(final String address) {
			if (!this.isAddress(address)) {
				throw new IllegalArgumentException(String.format(
						"invalid address: %s", address));
			}
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(address);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return inetAddress.getAddress();
		}
	};
	
	public static AddressType get(final String address) {
		for (AddressType addressType : AddressType.values()) {
			if (!addressType.equals(DOMAINNAME) && addressType.isAddress(address)) {
				return addressType;
			}
		}
		if (DOMAINNAME.isAddress(address)) {
			return DOMAINNAME;
		}
		throw new IllegalArgumentException(
				"no AddressType of the specified address: " + address);
	}
	
	public static AddressType valueOf(final byte b) {
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
	
	private final byte byteValue;
	
	private AddressType(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
	public abstract int getAddressLength(final byte firstByte);
	
	public abstract boolean isAddress(final String s);
	
	public abstract String readAddress(final byte[] b);
	
	public abstract byte[] writeAddress(final String address);
}
