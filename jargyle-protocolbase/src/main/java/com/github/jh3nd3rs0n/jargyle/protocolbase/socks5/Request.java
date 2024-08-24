package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A request to the SOCKS server.
 */
public final class Request {

    /**
     * The {@code byte} value for the reserved field.
     */
    private static final int RSV = 0x00;

    /**
     * The {@code Version} of this {@code Request}.
     */
    private final Version version;

    /**
     * The {@code Command} of this {@code Request}.
     */
    private final Command command;

    /**
     * The desired destination {@code Address} of this {@code Request}.
     */
    private final Address desiredDestinationAddress;

    /**
     * The desired destination {@code Port} of this {@code Request}.
     */
    private final Port desiredDestinationPort;

    /**
     * Constructs a {@code Request} with the provided {@code Command}, the
     * provided desired destination {@code Address}, and the provided desired
     * destination {@code Port}.
     *
     * @param cmd                    the provided {@code Command}
     * @param desiredDestinationAddr the provided desired destination
     *                               {@code Address}
     * @param desiredDestinationPrt  the provided desired destination
     *                               {@code Port}
     */
    private Request(
            final Command cmd,
            final Address desiredDestinationAddr,
            final Port desiredDestinationPrt) {
        this.version = Version.V5;
        this.command = cmd;
        this.desiredDestinationAddress = desiredDestinationAddr;
        this.desiredDestinationPort = desiredDestinationPrt;
    }

    /**
     * Returns a new {@code Request} with the provided {@code Command}, the
     * provided desired destination {@code Address}, and the provided desired
     * destination {@code Port}.
     *
     * @param cmd                    the provided {@code Command}
     * @param desiredDestinationAddr the provided desired destination
     *                               {@code Address}
     * @param desiredDestinationPrt  the provided desired destination
     *                               {@code Port}
     * @return a new {@code Request} with the provided {@code Command}, the
     * provided desired destination {@code Address}, and the provided desired
     * destination {@code Port}
     */
    public static Request newInstance(
            final Command cmd,
            final Address desiredDestinationAddr,
            final Port desiredDestinationPrt) {
        return new Request(
                Objects.requireNonNull(cmd),
                Objects.requireNonNull(desiredDestinationAddr),
                Objects.requireNonNull(desiredDestinationPrt));
    }

    /**
     * Returns a new {@code Request} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code Request}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static Request newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        Command cmd = readCommandFrom(in);
        UnsignedByte rsv = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        if (rsv.intValue() != RSV) {
            throw new Socks5Exception(String.format(
                    "expected RSV is %s, actual RSV is %s",
                    RSV, rsv.intValue()));
        }
        Address dstAddr = Address.newInstanceFrom(in);
        Port dstPort = PortIoHelper.readPortFrom(in);
        return newInstance(cmd, dstAddr, dstPort);
    }

    /**
     * Reads a {@code Command} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} has been reached.
     *
     * @param in the provided {@code InputStream}
     * @return a {@code Command}
     * @throws IOException if the end of the provided {@code InputStream} is
     *                     reached ({@code EOFException}) or if an I/O error
     *                     occurs
     */
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
        Request other = (Request) obj;
        if (!this.command.equals(other.command)) {
            return false;
        }
        if (!this.desiredDestinationAddress.equals(
                other.desiredDestinationAddress)) {
            return false;
        }
        return this.desiredDestinationPort.equals(
                other.desiredDestinationPort);
    }

    /**
     * Returns the {@code Command} of this {@code Request}.
     *
     * @return the {@code Command} of this {@code Request}
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * Returns the desired destination {@code Address} of this {@code Request}.
     *
     * @return the desired destination {@code Address} of this {@code Request}
     */
    public Address getDesiredDestinationAddress() {
        return this.desiredDestinationAddress;
    }

    /**
     * Returns the desired destination {@code Port} of this {@code Request}.
     *
     * @return the desired destination {@code Port} of this {@code Request}
     */
    public Port getDesiredDestinationPort() {
        return this.desiredDestinationPort;
    }

    /**
     * Returns the {@code Version} of this {@code Request}.
     *
     * @return the {@code Version} of this {@code Request}
     */
    public Version getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.version.hashCode();
        result = prime * result + this.command.hashCode();
        result = prime * result + this.desiredDestinationAddress.hashCode();
        result = prime * result + this.desiredDestinationPort.hashCode();
        return result;
    }

    /**
     * Returns the {@code byte} array of this {@code Request}.
     *
     * @return the {@code byte} array of this {@code Request}
     */
    public byte[] toByteArray() {
        byte[] dstAddr = this.desiredDestinationAddress.toByteArray();
        byte[] dstPort = PortIoHelper.toByteArray(
                this.desiredDestinationPort);
        byte[] arr = new byte[3 + dstAddr.length + dstPort.length];
        arr[0] = this.version.byteValue();
        arr[1] = this.command.byteValue();
        arr[2] = UnsignedByte.valueOf(RSV).byteValue();
        System.arraycopy(dstAddr, 0, arr, 3, dstAddr.length);
        System.arraycopy(dstPort, 0, arr, 3 + dstAddr.length, dstPort.length);
        return arr;
    }

    /**
     * Returns the {@code String} representation of this {@code Request}.
     *
     * @return the {@code String} representation of this {@code Request}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", command=" +
                this.command +
                ", desiredDestinationAddress=" +
                this.desiredDestinationAddress +
                ", desiredDestinationPort=" +
                this.desiredDestinationPort +
                "]";
    }


}
