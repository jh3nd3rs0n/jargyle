package com.github.jh3nd3rs0n.jargyle.protocolbase.internal;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Helper class for reading and writing {@code UnsignedShort}s.
 */
public final class UnsignedShortIoHelper {

    /**
     * The length of a byte array of an unsigned short.
     */
    private static final int BYTE_ARRAY_LENGTH = 2;

    /**
     * Prevents the construction of unnecessary instances.
     */
    private UnsignedShortIoHelper() {
    }

    /**
     * Reads an {@code UnsignedShort} from the provided {@code InputStream}.
     * An {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return an {@code UnsignedShort}
     * @throws IOException if the end of the provided {@code InputStream} is
     *                     reached ({@code EOFException}) or if an I/O error
	 *                     occurs
     */
    public static UnsignedShort readUnsignedShortFrom(
            final InputStream in) throws IOException {
        byte[] b = new byte[BYTE_ARRAY_LENGTH];
        int length = in.read(b);
        if (length != BYTE_ARRAY_LENGTH) {
            throw new EOFException(
                    "unexpected end of the input stream is reached");
        }
        ByteBuffer bb = ByteBuffer.wrap(b);
        return UnsignedShort.valueOf(bb.getShort());
    }

    /**
     * Converts the provided {@code UnsignedShort} to a {@code byte} array.
     *
     * @param unsignedShort the provided {@code UnsignedShort}
     * @return a {@code byte} array
     */
    public static byte[] toByteArray(final UnsignedShort unsignedShort) {
        ByteBuffer bb = ByteBuffer.allocate(BYTE_ARRAY_LENGTH);
        bb.putShort(unsignedShort.shortValue());
        return bb.array();
    }

    /**
     * Converts the provided {@code byte} array to an {@code UnsignedShort}.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code byte} array length is less than 2.
     *
     * @param b the provided {@code byte} array
     * @return an {@code UnsignedShort}
     */
    public static UnsignedShort toUnsignedShort(final byte[] b) {
        UnsignedShort unsignedShort;
        try {
            unsignedShort = readUnsignedShortFrom(new ByteArrayInputStream(b));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return unsignedShort;
    }

}
