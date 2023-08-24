package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.lang.UnsignedShort;

public final class Socks5Request {

	private static final class Params {
		private Version version;
		private Command command;
		private AddressType addressType;
		private String desiredDestinationAddress;
		private int desiredDestinationPort;
		private byte[] byteArray;
	}
	
	private static final int RSV = 0x00;
	
	private static final int MIN_DST_ADDR_LENGTH = 1;
	private static final int MAX_DST_ADDR_LENGTH = 255;
	
	public static Socks5Request newInstance(final byte[] b) {
		Socks5Request socks5Request;
		try {
			socks5Request = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return socks5Request;
	}
	
	public static Socks5Request newInstance(
			final Command command,
			final String desiredDestinationAddress,
			final int desiredDestinationPort) {
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
		if (desiredDestinationPort < UnsignedShort.MIN_INT_VALUE 
				|| desiredDestinationPort > UnsignedShort.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"desired destination port must be no less than %s and no "
					+ "more than %s", 
					UnsignedShort.MIN_INT_VALUE,
					UnsignedShort.MAX_INT_VALUE));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(UnsignedByte.newInstance(version.byteValue()).intValue());
		out.write(UnsignedByte.newInstance(command.byteValue()).intValue());
		out.write(RSV);
		Address address = Address.newInstance(desiredDestinationAddress);
		AddressType addressType = address.getAddressType();
		out.write(UnsignedByte.newInstance(addressType.byteValue()).intValue());
		try {
			out.write(address.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(UnsignedShort.newInstance(
					desiredDestinationPort).toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.version = version;
		params.command = command;
		params.addressType = addressType;
		params.desiredDestinationAddress = desiredDestinationAddress;
		params.desiredDestinationPort = desiredDestinationPort;
		params.byteArray = out.toByteArray();
		return new Socks5Request(params);
	}
	
	public static Socks5Request newInstanceFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = Version.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		Command cmd = Command.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(cmd.byteValue()).intValue());
		UnsignedByte rsv = UnsignedByte.newInstanceFrom(in);
		if (rsv.intValue() != RSV) {
			throw new Socks5Exception(String.format(
					"expected RSV is %s, actual RSV is %s", 
					RSV, rsv.intValue()));
		}
		out.write(rsv.intValue());
		Address addr = Address.newInstanceFrom(in);
		AddressType atyp = addr.getAddressType(); 
		out.write(UnsignedByte.newInstance(atyp.byteValue()).intValue());
		String dstAddr = addr.toString(); 
		out.write(addr.toByteArray());
		UnsignedShort dstPort = UnsignedShort.newInstanceFrom(in);
		out.write(dstPort.toByteArray());
		Params params = new Params();
		params.version = ver;
		params.command = cmd;
		params.addressType = atyp;
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort.intValue();
		params.byteArray = out.toByteArray();
		return new Socks5Request(params);
	}
	
	private final Version version;
	private final Command command;
	private final AddressType addressType;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final byte[] byteArray;
	
	private Socks5Request(final Params params) {
		this.version = params.version;
		this.command = params.command;
		this.addressType = params.addressType;
		this.desiredDestinationAddress = params.desiredDestinationAddress;
		this.desiredDestinationPort = params.desiredDestinationPort;
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
		Socks5Request other = (Socks5Request) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public AddressType getAddressType() {
		return this.addressType;
	}

	public Command getCommand() {
		return this.command;
	}

	public String getDesiredDestinationAddress() {
		return this.desiredDestinationAddress;
	}

	public int getDesiredDestinationPort() {
		return this.desiredDestinationPort;
	}

	public Version getVersion() {
		return this.version;
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
			.append(" [version=")
			.append(this.version)
			.append(", command=")
			.append(this.command)
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
