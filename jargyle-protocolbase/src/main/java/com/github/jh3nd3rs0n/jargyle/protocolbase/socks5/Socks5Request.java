package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class Socks5Request {

	private static final class Params {
		private Version version;
		private Command command;
		private AddressType addressType;
		private Address desiredDestinationAddress;
		private Port desiredDestinationPort;
		private byte[] byteArray;
	}
	
	private static final int RSV = 0x00;
	
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
			final Address desiredDestinationAddress,
			final Port desiredDestinationPort) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(UnsignedByte.newInstance(version.byteValue()).intValue());
		out.write(UnsignedByte.newInstance(command.byteValue()).intValue());
		out.write(RSV);
		AddressType addressType = desiredDestinationAddress.getAddressType();
		out.write(UnsignedByte.newInstance(addressType.byteValue()).intValue());
		try {
			out.write(desiredDestinationAddress.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(desiredDestinationPort.toByteArray());
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
		Address dstAddr = Address.newInstanceFrom(in);
		AddressType atyp = dstAddr.getAddressType(); 
		out.write(UnsignedByte.newInstance(atyp.byteValue()).intValue());
		out.write(dstAddr.toByteArray());
		Port dstPort = Port.newInstanceFrom(in);
		out.write(dstPort.toByteArray());
		Params params = new Params();
		params.version = ver;
		params.command = cmd;
		params.addressType = atyp;
		params.desiredDestinationAddress = dstAddr;
		params.desiredDestinationPort = dstPort;
		params.byteArray = out.toByteArray();
		return new Socks5Request(params);
	}
	
	private final Version version;
	private final Command command;
	private final AddressType addressType;
	private final Address desiredDestinationAddress;
	private final Port desiredDestinationPort;
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

	public Address getDesiredDestinationAddress() {
		return this.desiredDestinationAddress;
	}

	public Port getDesiredDestinationPort() {
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
