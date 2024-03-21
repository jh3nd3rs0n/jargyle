package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortIoHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public final class UdpRequestHeader {

    private static final int RSV = 0x0000;

    private final UnsignedByte currentFragmentNumber;
	private final Address desiredDestinationAddress;
	private final Port desiredDestinationPort;
	private final byte[] userData;
	
	private UdpRequestHeader(
			final UnsignedByte currentFragNumber,
			final Address desiredDestinationAddr,
			final Port desiredDestinationPrt,
			final byte[] usrData) {
		this.currentFragmentNumber = currentFragNumber;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.userData = Arrays.copyOf(usrData, usrData.length);
	}

	public static UdpRequestHeader newInstance(
			final UnsignedByte currentFragNumber,
			final Address desiredDestinationAddr,
			final Port desiredDestinationPrt,
			final byte[] usrData) {
		return new UdpRequestHeader(
				Objects.requireNonNull(currentFragNumber),
				Objects.requireNonNull(desiredDestinationAddr),
				Objects.requireNonNull(desiredDestinationPrt),
				Objects.requireNonNull(usrData));
	}

    public static UdpRequestHeader newInstanceFrom(final byte[] b) {
        UdpRequestHeader udpRequestHeader;
        try {
            udpRequestHeader = newInstanceFrom(new ByteArrayInputStream(b));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return udpRequestHeader;
    }

    private static UdpRequestHeader newInstanceFrom(
            final InputStream in) throws IOException {
        UnsignedShort rsv = UnsignedShortIoHelper.readUnsignedShortFrom(in);
        if (rsv.intValue() != RSV) {
             throw new IllegalArgumentException(String.format(
                     "expected RSV is %s, actual RSV is %s",
                     RSV, rsv.intValue()));
        }
        UnsignedByte frag = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        Address dstAddr = AddressHelper.readAddressFrom(in);
        Port dstPort = PortIoHelper.readPortFrom(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        return newInstance(frag, dstAddr, dstPort, out.toByteArray());
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
		if (!this.currentFragmentNumber.equals(other.currentFragmentNumber)) {
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
		if (!Arrays.equals(this.userData, other.userData)) {
			return false;
		}
		return true;
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
		return Arrays.copyOf(this.userData, this.userData.length);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.currentFragmentNumber.hashCode();
		result = prime * result + this.desiredDestinationAddress.hashCode();
		result = prime * result + this.desiredDestinationPort.hashCode();
		result = prime * result + Arrays.hashCode(this.userData);
		return result;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			out.write(UnsignedShortIoHelper.toByteArray(
					UnsignedShort.valueOf(RSV)));
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		out.write(this.currentFragmentNumber.intValue());
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
		try {
			out.write(this.userData);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return out.toByteArray();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [currentFragmentNumber=")
			.append(this.currentFragmentNumber)
			.append(", desiredDestinationAddress=")
			.append(this.desiredDestinationAddress)
			.append(", desiredDestinationPort=")
			.append(this.desiredDestinationPort)
			.append("]");
		return builder.toString();
	}

}
