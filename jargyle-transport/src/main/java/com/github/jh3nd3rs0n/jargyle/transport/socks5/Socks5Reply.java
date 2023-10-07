package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.net.AllZerosAddressConstants;

public final class Socks5Reply {

	private static final class Params {
		private Version version;
		private Reply reply;
		private AddressType addressType;
		private Address serverBoundAddress;
		private Port serverBoundPort;
		private byte[] byteArray;
	}
	
	private static final int RSV = 0x00;
	
	public static Socks5Reply newFailureInstance(final Reply reply) {
		if (reply.equals(Reply.SUCCEEDED)) {
			throw new IllegalArgumentException("reply must be of a failure");
		}
		return newInstance(
				reply, 
				Address.newInstance(AllZerosAddressConstants.IPV4_ADDRESS),
				Port.newInstance(0));
	}
	
	public static Socks5Reply newInstance(final byte[] b) {
		Socks5Reply socks5Reply;
		try {
			socks5Reply = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return socks5Reply;
	}
	
	public static Socks5Reply newInstance(
			final Reply reply,
			final Address serverBoundAddress,
			final Port serverBoundPort) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(UnsignedByte.newInstance(version.byteValue()).intValue());
		out.write(UnsignedByte.newInstance(reply.byteValue()).intValue());
		out.write(RSV);
		AddressType addressType = serverBoundAddress.getAddressType();
		out.write(UnsignedByte.newInstance(addressType.byteValue()).intValue());
		try {
			out.write(serverBoundAddress.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(serverBoundPort.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.version = version;
		params.reply = reply;
		params.addressType = addressType;
		params.serverBoundAddress = serverBoundAddress;
		params.serverBoundPort = serverBoundPort;
		params.byteArray = out.toByteArray();
		return new Socks5Reply(params);
	}
	
	public static Socks5Reply newInstanceFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = Version.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		Reply rep = Reply.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(rep.byteValue()).intValue());
		UnsignedByte rsv = UnsignedByte.newInstanceFrom(in);
		if (rsv.intValue() != RSV) {
			throw new Socks5Exception(String.format(
					"expected RSV is %s, actual RSV is %s", 
					RSV, rsv.intValue()));
		}
		out.write(rsv.intValue());
		Address bndAddr = Address.newInstanceFrom(in);
		AddressType atyp = bndAddr.getAddressType(); 
		out.write(UnsignedByte.newInstance(atyp.byteValue()).intValue());
		out.write(bndAddr.toByteArray());
		Port bndPort = Port.newInstanceFrom(in);
		out.write(bndPort.toByteArray());
		Params params = new Params();
		params.version = ver;
		params.reply = rep;
		params.addressType = atyp;
		params.serverBoundAddress = bndAddr;
		params.serverBoundPort = bndPort;
		params.byteArray = out.toByteArray();
		return new Socks5Reply(params);
	}
	
	private final Version version;
	private final Reply reply;
	private final AddressType addressType;
	private final Address serverBoundAddress;
	private final Port serverBoundPort;
	private final byte[] byteArray;
	
	private Socks5Reply(final Params params) {
		this.version = params.version;
		this.reply = params.reply;
		this.addressType = params.addressType;
		this.serverBoundAddress = params.serverBoundAddress;
		this.serverBoundPort = params.serverBoundPort;
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
		Socks5Reply other = (Socks5Reply) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public AddressType getAddressType() {
		return this.addressType;
	}

	public Reply getReply() {
		return this.reply;
	}

	public Address getServerBoundAddress() {
		return this.serverBoundAddress;
	}

	public Port getServerBoundPort() {
		return this.serverBoundPort;
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
			.append(", reply=")
			.append(this.reply)
			.append(", addressType=")
			.append(this.addressType)
			.append(", serverBoundAddress=")
			.append(this.serverBoundAddress)
			.append(", serverBoundPort=")
			.append(this.serverBoundPort)
			.append("]");
		return builder.toString();
	}
	
	
}
