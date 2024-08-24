package com.github.jh3nd3rs0n.jargyle.protocolbase.internal;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for reading and writing {@code UnsignedByte}s.
 */
public final class UnsignedByteIoHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private UnsignedByteIoHelper() {
    }

    /**
     * Reads an {@code UnsignedByte} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return an {@code UnsignedByte}
     * @throws IOException if the end of the provided {@code InputStream} is
     *                     reached ({@code EOFException}) or if an I/O error
     *                     occurs
     */
    public static UnsignedByte readUnsignedByteFrom(
            final InputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            throw new EOFException(
                    "unexpected end of the input stream is reached");
        }
        return UnsignedByte.valueOf(b);
    }

}
