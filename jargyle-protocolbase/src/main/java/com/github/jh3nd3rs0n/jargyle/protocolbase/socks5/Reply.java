package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A reply from the SOCKS server.
 */
public final class Reply {

    /**
     * The {@code byte} value for the reserved field.
     */
    private static final int RSV = 0x00;

    /**
     * The {@code Version} for this {@code Reply}.
     */
    private final Version version;

    /**
     * The {@code ReplyCode} for this {@code Reply}.
     */
    private final ReplyCode replyCode;

    /**
     * The server bound {@code Address} for this {@code Reply}.
     */
    private final Address serverBoundAddress;

    /**
     * The server bound {@code Port} for this {@code Reply}.
     */
    private final Port serverBoundPort;

    /**
     * Constructs a {@code Reply} with the provided {@code ReplyCode}, the
     * provided server bound {@code Address}, and the provided server bound
     * {@code Port}.
     *
     * @param rplyCode        the provided {@code ReplyCode}
     * @param serverBoundAddr the provided server bound {@code Address}
     * @param serverBoundPrt  the provided server bound {@code Port}
     */
    private Reply(
            final ReplyCode rplyCode,
            final Address serverBoundAddr,
            final Port serverBoundPrt) {
        this.version = Version.V5;
        this.replyCode = Objects.requireNonNull(rplyCode);
        this.serverBoundAddress = Objects.requireNonNull(serverBoundAddr);
        this.serverBoundPort = Objects.requireNonNull(serverBoundPrt);
    }

    /**
     * Returns a new failure {@code Reply} with the provided
     * {@code ReplyCode}, the server bound IPv4 {@code Address} of all zeros,
	 * and the server bound {@code Port} of zero. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code ReplyCode} is a {@code ReplyCode} of {@code SUCCEEDED}.
     *
     * @param replyCode the provided {@code ReplyCode}
     * @return a new failure {@code Reply} with the provided
     * {@code ReplyCode}, the server bound IPv4 {@code Address} of all zeros,
	 * and the server bound {@code Port} of zero
     */
    public static Reply newFailureInstance(final ReplyCode replyCode) {
        if (replyCode.equals(ReplyCode.SUCCEEDED)) {
            throw new IllegalArgumentException(
                    "reply code must a failure reply code");
        }
        return newInstance(
                replyCode,
                Address.newInstanceFrom(HostIpv4Address.ALL_ZEROS_IPV4_ADDRESS),
                Port.valueOf(0));
    }

    /**
     * Returns a new {@code Reply} with the provided {@code ReplyCode}, the
     * provided server bound {@code Address}, and the provided server bound
     * {@code Port}.
     *
     * @param rplyCode        the provided {@code ReplyCode}
     * @param serverBoundAddr the provided server bound {@code Address}
     * @param serverBoundPrt  the provided server bound {@code Port}
     * @return a new {@code Reply} with the provided {@code ReplyCode}, the
     * provided server bound {@code Address}, and the provided server bound
     * {@code Port}
     */
    public static Reply newInstance(
            final ReplyCode rplyCode,
            final Address serverBoundAddr,
            final Port serverBoundPrt) {
        return new Reply(
                Objects.requireNonNull(rplyCode),
                Objects.requireNonNull(serverBoundAddr),
                Objects.requireNonNull(serverBoundPrt));
    }

    /**
     * Returns a new {@code Reply} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code Reply}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static Reply newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        ReplyCode rep = readReplyCodeFrom(in);
        UnsignedByte rsv = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        if (rsv.intValue() != RSV) {
            throw new Socks5Exception(String.format(
                    "expected RSV is %s, actual RSV is %s",
                    RSV, rsv.intValue()));
        }
        Address bndAddr = Address.newInstanceFrom(in);
        Port bndPort = PortIoHelper.readPortFrom(in);
        return newInstance(rep, bndAddr, bndPort);
    }

    /**
     * Returns a new success {@code Reply} with the {@code ReplyCode} of
     * {@code SUCCEEDED}, the provided server bound {@code Address}, and the
     * provided server bound {@code Port}.
     *
     * @param serverBoundAddr the provided server bound {@code Address}
     * @param serverBoundPrt  the provided server bound {@code Port}
     * @return a new success {@code Reply} with the {@code ReplyCode} of
     * {@code SUCCEEDED}, the provided server bound {@code Address}, and the
     * provided server bound {@code Port}
     */
    public static Reply newSuccessInstance(
            final Address serverBoundAddr,
            final Port serverBoundPrt) {
        return newInstance(ReplyCode.SUCCEEDED, serverBoundAddr, serverBoundPrt);
    }

    /**
     * Reads a {@code ReplyCode} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a {@code ReplyCode}
     * @throws IOException if the end of the provided {@code InputStream} is
     *                     reached ({@code EOFException}) or if an I/O error
     *                     occurs
     */
    private static ReplyCode readReplyCodeFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        ReplyCode replyCode;
        try {
            replyCode = ReplyCode.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new Socks5Exception(e);
        }
        return replyCode;
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
        Reply other = (Reply) obj;
        if (!this.replyCode.equals(other.replyCode)) {
            return false;
        }
        if (!this.serverBoundAddress.equals(other.serverBoundAddress)) {
            return false;
        }
        return this.serverBoundPort.equals(other.serverBoundPort);
    }

    /**
     * Returns the {@code ReplyCode} of this {@code Reply}.
     *
     * @return the {@code ReplyCode} of this {@code Reply}
     */
    public ReplyCode getReplyCode() {
        return this.replyCode;
    }

    /**
     * Returns the server bound {@code Address} of this {@code Reply}.
     *
     * @return the server bound {@code Address} of this {@code Reply}
     */
    public Address getServerBoundAddress() {
        return this.serverBoundAddress;
    }

    /**
     * Returns the server bound {@code Port} of this {@code Reply}.
     *
     * @return the server bound {@code Port} of this {@code Reply}
     */
    public Port getServerBoundPort() {
        return this.serverBoundPort;
    }

    /**
     * Returns the {@code Version} of this {@code Reply}.
     *
     * @return the {@code Version} of this {@code Reply}
     */
    public Version getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.version.hashCode();
        result = prime * result + this.replyCode.hashCode();
        result = prime * result + this.serverBoundAddress.hashCode();
        result = prime * result + this.serverBoundPort.hashCode();
        return result;
    }

    /**
     * Returns the {@code byte} array of this {@code Reply}.
     *
     * @return the {@code byte} array of this {@code Reply}
     */
    public byte[] toByteArray() {
        byte[] bndAddr = this.serverBoundAddress.toByteArray();
        byte[] bndPort = PortIoHelper.toByteArray(this.serverBoundPort);
        byte[] arr = new byte[3 + bndAddr.length + bndPort.length];
        arr[0] = this.version.byteValue();
        arr[1] = this.replyCode.byteValue();
        arr[2] = UnsignedByte.valueOf(RSV).byteValue();
        System.arraycopy(bndAddr, 0, arr, 3, bndAddr.length);
        System.arraycopy(bndPort, 0, arr, 3 + bndAddr.length, bndPort.length);
        return arr;
    }

    /**
     * Returns the {@code String} representation of this {@code Reply}.
     *
     * @return the {@code String} representation of this {@code Reply}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", replyCode=" +
                this.replyCode +
                ", serverBoundAddress=" +
                this.serverBoundAddress +
                ", serverBoundPort=" +
                this.serverBoundPort +
                "]";
    }


}
