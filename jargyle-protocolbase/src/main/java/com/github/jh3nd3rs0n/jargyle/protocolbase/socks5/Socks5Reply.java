package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class Socks5Reply {

	static final class Params {
		Version version;
		Reply reply;
		AddressType addressType;
		Address serverBoundAddress;
		Port serverBoundPort;
		byte[] byteArray;
	}
	
	static final int RSV = 0x00;
	
	public static Socks5Reply newFailureInstance(final Reply reply) {
		if (reply.equals(Reply.SUCCEEDED)) {
			throw new IllegalArgumentException("reply must be of a failure");
		}
		return newInstance(
				reply, 
				Address.newInstance(HostIpv4Address.ALL_ZEROS_IPV4_ADDRESS),
				Port.newInstanceOf(0));
	}
	
	public static Socks5Reply newInstance(final byte[] b) {
		Socks5Reply socks5Reply = null;
		try {
			socks5Reply = Socks5ReplyInputHelper.readSocks5ReplyFrom(
					new ByteArrayInputStream(b));
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
		out.write(UnsignedByte.newInstanceOf(version.byteValue()).intValue());
		out.write(UnsignedByte.newInstanceOf(reply.byteValue()).intValue());
		out.write(RSV);
		try {
			out.write(serverBoundAddress.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(serverBoundPort.toUnsignedShort().toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.version = version;
		params.reply = reply;
		params.addressType = serverBoundAddress.getAddressType();
		params.serverBoundAddress = serverBoundAddress;
		params.serverBoundPort = serverBoundPort;
		params.byteArray = out.toByteArray();
		return new Socks5Reply(params);
	}
	
	private final Version version;
	private final Reply reply;
	private final AddressType addressType;
	private final Address serverBoundAddress;
	private final Port serverBoundPort;
	private final byte[] byteArray;
	
	Socks5Reply(final Params params) {
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
