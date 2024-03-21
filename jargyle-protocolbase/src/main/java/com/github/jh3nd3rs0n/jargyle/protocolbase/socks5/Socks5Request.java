package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class Socks5Request {

    private static final int RSV = 0x00;

    private final Version version;
	private final Command command;
	private final Address desiredDestinationAddress;
	private final Port desiredDestinationPort;

	private Socks5Request(
			final Command cmd,
			final Address desiredDestinationAddr,
			final Port desiredDestinationPrt) {
		this.version = Version.V5;
		this.command = cmd;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
	}

	public static Socks5Request newInstance(
			final Command cmd,
			final Address desiredDestinationAddr,
			final Port desiredDestinationPrt) {
		return new Socks5Request(
				Objects.requireNonNull(cmd),
				Objects.requireNonNull(desiredDestinationAddr),
				Objects.requireNonNull(desiredDestinationPrt));
	}

    public static Socks5Request newInstanceFrom(final byte[] b) {
        Socks5Request socks5Request;
        try {
            socks5Request = newInstanceFrom(new ByteArrayInputStream(b));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return socks5Request;
    }

    public static Socks5Request newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        Command cmd = readCommandFrom(in);
        UnsignedByte rsv = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        if (rsv.intValue() != RSV) {
            throw new Socks5Exception(String.format(
                    "expected RSV is %s, actual RSV is %s",
                    RSV, rsv.intValue()));
        }
        Address dstAddr = AddressHelper.readAddressFrom(in);
        Port dstPort = PortIoHelper.readPortFrom(in);
        return newInstance(cmd, dstAddr, dstPort);
    }

    private static Command readCommandFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        Command command;
        try {
            command = Command.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new CommandNotSupportedException(b);
        }
        return command;
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
		if (!this.command.equals(other.command)) {
			return false;
		}
		if (!this.desiredDestinationAddress.equals(
				other.desiredDestinationAddress)) {
			return false;
		}
		if (!this.desiredDestinationPort.equals(
				other.desiredDestinationPort)) {
			return false;
		}
		return true;
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
		result = prime * result + this.command.hashCode();
		result = prime * result + this.desiredDestinationAddress.hashCode();
		result = prime * result + this.desiredDestinationPort.hashCode();
		return result;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsignedByte.valueOf(
				this.version.byteValue()).intValue());
		out.write(UnsignedByte.valueOf(
				this.command.byteValue()).intValue());
		out.write(RSV);
		try {
			out.write(AddressHelper.toByteArray(
					this.desiredDestinationAddress));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try {
			out.write(PortIoHelper.toByteArray(
					this.desiredDestinationPort));
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
			.append(", command=")
			.append(this.command)
			.append(", desiredDestinationAddress=")
			.append(this.desiredDestinationAddress)
			.append(", desiredDestinationPort=")
			.append(this.desiredDestinationPort)
			.append("]");
		return builder.toString();
	}
	
	
}
