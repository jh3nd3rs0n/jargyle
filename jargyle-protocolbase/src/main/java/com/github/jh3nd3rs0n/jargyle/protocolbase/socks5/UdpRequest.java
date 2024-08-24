package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortIoHelper;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * A forwarded datagram to/from the UDP relay server.
 */
public final class UdpRequest {

    /**
     * The {@code byte} values for the reserved field.
     */
    private static final int RSV = 0x0000;

    /**
     * The current fragment number of this {@code UdpRequest}.
     */
    private final UnsignedByte currentFragmentNumber;

    /**
     * The desired destination {@code Address} of this {@code UdpRequest}.
     */
    private final Address desiredDestinationAddress;

    /**
     * The desired destination {@code Port} of this {@code UdpRequest}.
     */
    private final Port desiredDestinationPort;

    /**
     * The user data of this {@code UdpRequest}.
     */
    private final byte[] userData;

    /**
     * Constructs a {@code UdpRequest} with the provided current fragment
     * number, the provided desired destination {@code Address}, the provided
     * desired destination {@code Port}, and the provided user data
     *
     * @param currentFragNumber      the provided current fragment number
     * @param desiredDestinationAddr the provided desired destination
     *                               {@code Address}
     * @param desiredDestinationPrt  the provided desired destination
     *                               {@code Port}
     * @param usrData                the provided user data
     */
    private UdpRequest(
            final UnsignedByte currentFragNumber,
            final Address desiredDestinationAddr,
            final Port desiredDestinationPrt,
            final byte[] usrData) {
        this.currentFragmentNumber = currentFragNumber;
        this.desiredDestinationAddress = desiredDestinationAddr;
        this.desiredDestinationPort = desiredDestinationPrt;
        this.userData = Arrays.copyOf(usrData, usrData.length);
    }

    /**
     * Returns a new {@code UdpRequest} with the provided current fragment
     * number, the provided desired destination {@code Address}, the provided
     * desired destination {@code Port}, and the provided user data.
     *
     * @param currentFragNumber      the provided current fragment number
     * @param desiredDestinationAddr the provided desired destination
     *                               {@code Address}
     * @param desiredDestinationPrt  the provided desired destination
     *                               {@code Port}
     * @param usrData                the provided user data
     * @return a new {@code UdpRequest} with the provided current fragment
     * number, the provided desired destination {@code Address}, the provided
     * desired destination {@code Port}, and the provided user data
     */
    public static UdpRequest newInstance(
            final UnsignedByte currentFragNumber,
            final Address desiredDestinationAddr,
            final Port desiredDestinationPrt,
            final byte[] usrData) {
        return new UdpRequest(
                Objects.requireNonNull(currentFragNumber),
                Objects.requireNonNull(desiredDestinationAddr),
                Objects.requireNonNull(desiredDestinationPrt),
                Objects.requireNonNull(usrData));
    }

    /**
     * Returns a new {@code UdpRequest} from the provided {@code byte} array.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code byte} array is invalid.
     *
     * @param b the provided {@code byte} array
     * @return a new {@code UdpRequest}
     */
    public static UdpRequest newInstanceFrom(final byte[] b) {
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        UdpRequest udpRequest;
        try {
            udpRequest = newInstanceFrom(in);
        } catch (EOFException e) {
            throw new IllegalArgumentException("unexpected end of byte array");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return udpRequest;
    }

    /**
     * Returns a new {@code UdpRequest} from the provided {@code InputStream}.
     * An {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code UdpRequest}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    private static UdpRequest newInstanceFrom(
            final InputStream in) throws IOException {
        UnsignedShort rsv = UnsignedShortIoHelper.readUnsignedShortFrom(in);
        if (rsv.intValue() != RSV) {
            throw new Socks5Exception(String.format(
                    "expected RSV is %s, actual RSV is %s",
                    RSV, rsv.intValue()));
        }
        UnsignedByte frag = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        Address dstAddr = Address.newInstanceFrom(in);
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
        UdpRequest other = (UdpRequest) obj;
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
        return Arrays.equals(this.userData, other.userData);
    }

    /**
     * Returns the current fragment number of this {@code UdpRequest}.
     *
     * @return the current fragment number of this {@code UdpRequest}
     */
    public UnsignedByte getCurrentFragmentNumber() {
        return this.currentFragmentNumber;
    }

    /**
     * Returns the desired destination {@code Address} of this
     * {@code UdpRequest}.
     *
     * @return the desired destination {@code Address} of this
     * {@code UdpRequest}
     */
    public Address getDesiredDestinationAddress() {
        return this.desiredDestinationAddress;
    }

    /**
     * Returns the desired destination {@code Port} of this
     * {@code UdpRequest}.
     *
     * @return the desired destination {@code Port} of this
     * {@code UdpRequest}
     */
    public Port getDesiredDestinationPort() {
        return this.desiredDestinationPort;
    }

    /**
     * Returns the user data of this {@code UdpRequest}.
     *
     * @return the user data of this {@code UdpRequest}
     */
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

    /**
     * Returns the {@code byte} array of this {@code UdpRequest}.
     *
     * @return the {@code byte} array of this {@code UdpRequest}
     */
    public byte[] toByteArray() {
        byte[] rsv = UnsignedShortIoHelper.toByteArray(
                UnsignedShort.valueOf(RSV));
        byte[] dstAddr = this.desiredDestinationAddress.toByteArray();
        byte[] dstPort = PortIoHelper.toByteArray(
                this.desiredDestinationPort);
        byte[] arr = new byte[
                rsv.length + 1 + dstAddr.length + dstPort.length + this.userData.length];
        System.arraycopy(rsv, 0, arr, 0, rsv.length);
        arr[rsv.length] = this.currentFragmentNumber.byteValue();
        System.arraycopy(dstAddr, 0, arr, rsv.length + 1, dstAddr.length);
        System.arraycopy(
                dstPort,
                0,
                arr,
                rsv.length + 1 + dstAddr.length,
                dstPort.length);
        System.arraycopy(
                this.userData,
                0,
                arr,
                rsv.length + 1 + dstAddr.length + dstPort.length,
                this.userData.length);
        return arr;
    }

    /**
     * Returns the {@code String} representation of this {@code UdpRequest}.
     *
     * @return the {@code String} representation of this {@code UdpRequest}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [currentFragmentNumber=" +
                this.currentFragmentNumber +
                ", desiredDestinationAddress=" +
                this.desiredDestinationAddress +
                ", desiredDestinationPort=" +
                this.desiredDestinationPort +
                "]";
    }

}
