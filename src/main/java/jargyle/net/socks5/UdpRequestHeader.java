package jargyle.net.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import jargyle.util.UnsignedByte;
import jargyle.util.UnsignedShort;

public final class UdpRequestHeader {

	private static final class Params {
		private int currentFragmentNumber;
		private AddressType addressType;
		private String desiredDestinationAddress;
		private int desiredDestinationPort;
		private int userDataStartIndex;
		private byte[] byteArray;
	}
	
	private static final int RSV = 0x0000;
	
	private static final int MIN_DST_ADDR_LENGTH = 1;
	private static final int MAX_DST_ADDR_LENGTH = 255;
	
	public static UdpRequestHeader newInstance(final byte[] byteArray) {
		int b = -1;
		ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
		int dataStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] bytes = new byte[UnsignedShort.BYTE_ARRAY_LENGTH];
		int bytesRead = -1;
		try {
			bytesRead = in.read(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		int rsv = UnsignedShort.newInstance(bytes).intValue();
		if (rsv != RSV) {
			 throw new IllegalArgumentException(String.format(
					 "expected RSV is %s, actual RSV is %s", RSV, rsv));
		}
		dataStartIndex += bytes.length;
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		b = in.read();
		int frag = UnsignedByte.newInstance(b).intValue();
		dataStartIndex++;
		out.write(b);
		b = in.read();
		AddressType atyp = AddressType.valueOf(
				(byte) UnsignedByte.newInstance(b).intValue());
		dataStartIndex++;
		out.write(b);
		b = in.read();
		int dstAddrLength = atyp.getAddressLength(
				(byte) UnsignedByte.newInstance(b).intValue());
		bytes = new byte[dstAddrLength];
		bytes[0] = (byte) UnsignedByte.newInstance(b).intValue();
		bytesRead = in.read(bytes, 1, dstAddrLength - 1);
		bytes = Arrays.copyOf(bytes, bytesRead + 1);
		String dstAddr = atyp.readAddress(bytes);
		dataStartIndex += bytes.length;
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		bytes = new byte[UnsignedShort.BYTE_ARRAY_LENGTH];
		try {
			bytesRead = in.read(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		int dstPort = UnsignedShort.newInstance(bytes).intValue();
		dataStartIndex += bytes.length;
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		dataStartIndex++;
		bytes = new byte[byteArray.length - dataStartIndex + 1];
		try {
			bytesRead = in.read(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.currentFragmentNumber = frag;
		params.addressType = atyp;
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort;
		params.userDataStartIndex = dataStartIndex;
		params.byteArray = out.toByteArray();
		return new UdpRequestHeader(params);
	}
	
	public static UdpRequestHeader newInstance(
			final int currentFragmentNumber,
			final AddressType addressType,
			final String desiredDestinationAddress,
			final int desiredDestinationPort,
			final byte[] userData) {
		if (currentFragmentNumber < UnsignedByte.MIN_INT_VALUE 
				|| currentFragmentNumber > UnsignedByte.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"current fragment number must be no less than %s and no more than %s",
					UnsignedByte.MIN_INT_VALUE,
					UnsignedByte.MAX_INT_VALUE));
		}
		validateDesiredDestinationAddress(desiredDestinationAddress);
		validateDesiredDestinationPort(desiredDestinationPort);
		int dataStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] rsv = UnsignedShort.newInstance(RSV).toByteArray();
		dataStartIndex += rsv.length;
		try {
			out.write(rsv);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		dataStartIndex++;
		out.write(currentFragmentNumber);
		dataStartIndex++;
		out.write(addressType.byteValue());
		byte[] address = addressType.writeAddress(desiredDestinationAddress);
		dataStartIndex += address.length;
		try {
			out.write(address);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		byte[] port = UnsignedShort.newInstance(
				desiredDestinationPort).toByteArray();
		dataStartIndex += port.length;
		try {
			out.write(port);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		dataStartIndex++;
		try {
			out.write(userData);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.currentFragmentNumber = currentFragmentNumber;
		params.addressType = addressType;
		params.desiredDestinationAddress = desiredDestinationAddress;
		params.desiredDestinationPort = desiredDestinationPort;
		params.userDataStartIndex = dataStartIndex;
		params.byteArray = out.toByteArray();
		return new UdpRequestHeader(params);
	}
	
	public static void validateDesiredDestinationAddress(
			final String desiredDestinationAddress) {
		byte[] desiredDestinationAddressBytes = desiredDestinationAddress.getBytes();
		if (desiredDestinationAddressBytes.length < MIN_DST_ADDR_LENGTH
				|| desiredDestinationAddressBytes.length > MAX_DST_ADDR_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"desired destination address must be no less than %s byte(s) and no more than %s byte(s)", 
					MIN_DST_ADDR_LENGTH,
					MAX_DST_ADDR_LENGTH));
		}
	}
	
	public static void validateDesiredDestinationPort(
			final int desiredDestinationPort) {
		if (desiredDestinationPort < UnsignedShort.MIN_INT_VALUE 
				|| desiredDestinationPort > UnsignedShort.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"desired destination port must be no less than %s and no more than %s", 
					UnsignedShort.MIN_INT_VALUE,
					UnsignedShort.MAX_INT_VALUE));
		}
	}
	
	private final int currentFragmentNumber;
	private final AddressType addressType;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final int userDataStartIndex;
	private final byte[] byteArray;
	
	private UdpRequestHeader(final Params params) {
		this.currentFragmentNumber = params.currentFragmentNumber;
		this.addressType = params.addressType;
		this.desiredDestinationAddress = params.desiredDestinationAddress;
		this.desiredDestinationPort = params.desiredDestinationPort;
		this.userDataStartIndex = params.userDataStartIndex;
		this.byteArray = params.byteArray;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		UdpRequestHeader other = (UdpRequestHeader) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public AddressType getAddressType() {
		return this.addressType;
	}

	public int getCurrentFragmentNumber() {
		return this.currentFragmentNumber;
	}

	public String getDesiredDestinationAddress() {
		return this.desiredDestinationAddress;
	}

	public int getDesiredDestinationPort() {
		return this.desiredDestinationPort;
	}
	
	public byte[] getUserData() {
		return Arrays.copyOfRange(
				this.byteArray, this.userDataStartIndex, this.byteArray.length);
	}

	public int getUserDataStartIndex() {
		return this.userDataStartIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.byteArray);
		return result;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(this.byteArray, this.byteArray.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [currentFragmentNumber=")
			.append(this.currentFragmentNumber)
			.append(", addressType=")
			.append(this.addressType)
			.append(", desiredDestinationAddress=")
			.append(this.desiredDestinationAddress)
			.append(", desiredDestinationPort=")
			.append(this.desiredDestinationPort)
			.append(", userDataStartIndex=")
			.append(this.userDataStartIndex)
			.append(", userData=")
			.append(Arrays.toString(this.getUserData()))
			.append("]");
		return builder.toString();
	}
	
	
}
