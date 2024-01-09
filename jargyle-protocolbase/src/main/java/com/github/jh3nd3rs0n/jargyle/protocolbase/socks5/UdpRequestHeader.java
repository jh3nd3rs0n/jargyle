package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

public final class UdpRequestHeader {

	static final class Params {
		UnsignedByte currentFragmentNumber;
		AddressType addressType;
		Address desiredDestinationAddress;
		Port desiredDestinationPort;
		int userDataStartIndex;
		byte[] byteArray;
	}
	
	static final int RSV = 0x0000;
	
	public static UdpRequestHeader newInstance(final byte[] b) {
		UdpRequestHeader udpRequestHeader = null;
		try {
			udpRequestHeader = 
					UdpRequestHeaderInputHelper.readUdpRequestHeaderFrom(
							new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return udpRequestHeader;
	}
	
	public static UdpRequestHeader newInstance(
			final UnsignedByte currentFragmentNumber,
			final Address desiredDestinationAddress,
			final Port desiredDestinationPort,
			final byte[] userData) {
		int dataStartIndex = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] rsv = UnsignedShort.newInstanceOf(RSV).toByteArray();
		dataStartIndex += rsv.length;
		try {
			out.write(rsv);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		dataStartIndex += 2;
		out.write(currentFragmentNumber.intValue());
		byte[] bytes = desiredDestinationAddress.toByteArray();
		dataStartIndex += bytes.length;
		try {
			out.write(bytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		byte[] port = desiredDestinationPort.toUnsignedShort().toByteArray();
		dataStartIndex += port.length;
		try {
			out.write(port);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(userData);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.currentFragmentNumber = currentFragmentNumber;
		params.addressType = desiredDestinationAddress.getAddressType();
		params.desiredDestinationAddress = desiredDestinationAddress;
		params.desiredDestinationPort = desiredDestinationPort;
		params.userDataStartIndex = dataStartIndex;
		params.byteArray = out.toByteArray();
		return new UdpRequestHeader(params);
	}
	
	private final UnsignedByte currentFragmentNumber;
	private final AddressType addressType;
	private final Address desiredDestinationAddress;
	private final Port desiredDestinationPort;
	private final int userDataStartIndex;
	private final byte[] byteArray;
	
	UdpRequestHeader(final Params params) {
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

	public UnsignedByte getCurrentFragmentNumber() {
		return this.currentFragmentNumber;
	}

	public Address getDesiredDestinationAddress() {
		return this.desiredDestinationAddress;
	}

	public Port getDesiredDestinationPort() {
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
