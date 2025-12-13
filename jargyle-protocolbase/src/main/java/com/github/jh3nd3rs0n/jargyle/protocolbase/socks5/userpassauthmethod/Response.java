package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * A response from the server.
 */
public final class Response {

    /**
     * The status {@code byte} value that indicates success.
     */
    public static final byte STATUS_SUCCESS_BYTE_VALUE = 0x0;

    /**
     * The status {@code UnsignedByte} that indicates success.
     */
    public static final UnsignedByte STATUS_SUCCESS = UnsignedByte.valueOf(
            STATUS_SUCCESS_BYTE_VALUE);

    /**
     * The {@code Version} of this {@code Response}.
     */
    private final Version version;

    /**
     * The status {@code UnsignedByte} of this {@code Response}.
     */
    private final UnsignedByte status;

    /**
     * Constructs a {@code Response} with the provided status
     * {@code UnsignedByte}.
     *
     * @param stat the provided status {@code UnsignedByte}
     */
    private Response(final UnsignedByte stat) {
        this.version = Version.V1;
        this.status = stat;
    }

    /**
     * Returns a new {@code Response} with the provided status
     * {@code UnsignedByte}.
     *
     * @param stat the provided status {@code UnsignedByte}
     * @return a new {@code Response} with the provided status
     * {@code UnsignedByte}
     */
    public static Response newInstance(final UnsignedByte stat) {
        return new Response(stat);
    }

    /**
     * Returns a new {@code Response} from the provided {@code InputStream}.
     * An {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code Response}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static Response newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte status = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        return newInstance(status);
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
        Response other = (Response) obj;
        return this.status.equals(other.status);
    }

    /**
     * Returns the status {@code UnsignedByte} of this {@code Response}.
     *
     * @return the status {@code UnsignedByte} of this {@code Response}
     */
    public UnsignedByte getStatus() {
        return this.status;
    }

    /**
     * Returns the {@code Version} of this {@code Response}.
     *
     * @return the {@code Version} of this {@code Response}
     */
    public Version getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
		result = prime * result + this.version.hashCode();
        result = prime * result + this.status.hashCode();
        return result;
    }

    /**
     * Returns the {@code byte} array of this {@code Response}.
     *
     * @return the {@code byte} array of this {@code Response}
     */
    public byte[] toByteArray() {
        return new byte[] {
                this.version.byteValue(), this.status.byteValue()
        };
    }

    /**
     * Returns the {@code String} representation of this {@code Response}.
     *
     * @return the {@code String} representation of this {@code Response}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", status=" +
                this.status +
                "]";
    }

}
