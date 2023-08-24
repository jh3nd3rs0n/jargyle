package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.lang.UnsignedShort;

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
		ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
		int dataStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		UnsignedShort rsv;
		try {
			rsv = UnsignedShort.newInstanceFrom(in);
		} catch (IOException e) {
			throw new IllegalArgumentException("expected RSV", e);
		}
		if (rsv.intValue() != RSV) {
			 throw new IllegalArgumentException(String.format(
					 "expected RSV is %s, actual RSV is %s", 
					 RSV, rsv.intValue()));
		}
		byte[] bytes = rsv.toByteArray();
		dataStartIndex += bytes.length;
		try {
			out.write(rsv.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		UnsignedByte frag;
		try {
			frag = UnsignedByte.newInstanceFrom(in);
		} catch (IOException e) {
			throw new IllegalArgumentException("expected fragment number", e);
		}
		dataStartIndex++;
		out.write(frag.intValue());
		Address addr;
		try {
			addr = Address.newInstanceFrom(in);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"expected desired destination address type and address", e);
		}
		AddressType atyp = addr.getAddressType();
		dataStartIndex++;
		out.write(UnsignedByte.newInstance(atyp.byteValue()).intValue());
		bytes = addr.toByteArray();
		String dstAddr = addr.toString(); 
		dataStartIndex += bytes.length;
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		UnsignedShort dstPort;
		try {
			dstPort = UnsignedShort.newInstanceFrom(in);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"expected desired destination port", e);
		}
		bytes = dstPort.toByteArray();
		dataStartIndex += bytes.length;
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		dataStartIndex++;
		bytes = new byte[byteArray.length - dataStartIndex + 1];
		int bytesRead;
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
		params.currentFragmentNumber = frag.intValue();
		params.addressType = atyp;
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort.intValue();
		params.userDataStartIndex = dataStartIndex;
		params.byteArray = out.toByteArray();
		return new UdpRequestHeader(params);
	}
	
	public static UdpRequestHeader newInstance(
			final int currentFragmentNumber,
			final String desiredDestinationAddress,
			final int desiredDestinationPort,
			final byte[] userData) {
		if (currentFragmentNumber < UnsignedByte.MIN_INT_VALUE 
				|| currentFragmentNumber > UnsignedByte.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"current fragment number must be no less than %s and no "
					+ "more than %s",
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
		Address address = Address.newInstance(desiredDestinationAddress);
		AddressType addressType = address.getAddressType();
		dataStartIndex++;
		out.write(UnsignedByte.newInstance(addressType.byteValue()).intValue());
		byte[] bytes = address.toByteArray();
		dataStartIndex += bytes.length;
		try {
			out.write(bytes);
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
		byte[] desiredDestinationAddressBytes = 
				desiredDestinationAddress.getBytes();
		if (desiredDestinationAddressBytes.length < MIN_DST_ADDR_LENGTH
				|| desiredDestinationAddressBytes.length > MAX_DST_ADDR_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"desired destination address must be no less than %s "
					+ "byte(s) and no more than %s byte(s)", 
					MIN_DST_ADDR_LENGTH,
					MAX_DST_ADDR_LENGTH));
		}
	}
	
	public static void validateDesiredDestinationPort(
			final int desiredDestinationPort) {
		if (desiredDestinationPort < UnsignedShort.MIN_INT_VALUE 
				|| desiredDestinationPort > UnsignedShort.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"desired destination port must be no less than %s and no "
					+ "more than %s", 
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
			.append("]");
		return builder.toString();
	}
	
	
}
