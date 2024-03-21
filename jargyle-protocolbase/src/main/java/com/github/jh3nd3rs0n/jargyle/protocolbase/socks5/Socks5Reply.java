package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class Socks5Reply {

    private static final int RSV = 0x00;

	private final Version version;
	private final Reply reply;
	private final Address serverBoundAddress;
	private final Port serverBoundPort;

	private Socks5Reply(
			final Reply rply,
			final Address serverBoundAddr,
			final Port serverBoundPrt) {
		this.version = Version.V5;
		this.reply = Objects.requireNonNull(rply);
		this.serverBoundAddress = Objects.requireNonNull(serverBoundAddr);
		this.serverBoundPort = Objects.requireNonNull(serverBoundPrt);
	}

	public static Socks5Reply newFailureInstance(final Reply reply) {
		if (reply.equals(Reply.SUCCEEDED)) {
			throw new IllegalArgumentException("reply must be of a failure");
		}
		return newInstance(
				reply,
				Address.newInstance(HostIpv4Address.ALL_ZEROS_IPV4_ADDRESS),
				Port.valueOf(0));
	}

	public static Socks5Reply newInstance(
			final Reply rply,
			final Address serverBoundAddr,
			final Port serverBoundPrt) {
		return new Socks5Reply(
				Objects.requireNonNull(rply),
				Objects.requireNonNull(serverBoundAddr),
				Objects.requireNonNull(serverBoundPrt));
	}

    public static Socks5Reply newInstanceFrom(final byte[] b) {
        Socks5Reply socks5Reply;
        try {
            socks5Reply = newInstanceFrom(new ByteArrayInputStream(b));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return socks5Reply;
    }

    public static Socks5Reply newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        Reply rep = readReplyFrom(in);
        UnsignedByte rsv = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        if (rsv.intValue() != RSV) {
            throw new Socks5Exception(String.format(
                    "expected RSV is %s, actual RSV is %s",
                    RSV, rsv.intValue()));
        }
        Address bndAddr = AddressHelper.readAddressFrom(in);
        Port bndPort = PortIoHelper.readPortFrom(in);
        return newInstance(rep, bndAddr, bndPort);
    }

    private static Reply readReplyFrom(
			final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        Reply reply;
        try {
            reply = Reply.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new Socks5Exception(e);
        }
        return reply;
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
		if (!this.reply.equals(other.reply)) {
			return false;
		}
		if (!this.serverBoundAddress.equals(other.serverBoundAddress)) {
			return false;
		}
		if (!this.serverBoundPort.equals(other.serverBoundPort)) {
			return false;
		}
		return true;
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
		result = prime * result + this.reply.hashCode();
		result = prime * result + this.serverBoundAddress.hashCode();
		result = prime * result + this.serverBoundPort.hashCode();
		return result;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.valueOf(
				this.version.byteValue()).intValue());
		out.write(UnsignedByte.valueOf(
				this.reply.byteValue()).intValue());
		out.write(RSV);
		try {
			out.write(AddressHelper.toByteArray(this.serverBoundAddress));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(PortIoHelper.toByteArray(this.serverBoundPort));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return out.toByteArray();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [version=")
			.append(this.version)
			.append(", reply=")
			.append(this.reply)
			.append(", serverBoundAddress=")
			.append(this.serverBoundAddress)
			.append(", serverBoundPort=")
			.append(this.serverBoundPort)
			.append("]");
		return builder.toString();
	}
	
	
}
