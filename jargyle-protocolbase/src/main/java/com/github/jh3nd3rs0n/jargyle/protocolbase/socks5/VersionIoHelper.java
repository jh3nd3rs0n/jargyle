package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for reading and writing {@code Version}s.
 */
final class VersionIoHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private VersionIoHelper() {
    }

    /**
     * Reads the {@code Version} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @throws IOException if the end of the provided {@code InputStream} is
     *                     reached ({@code EOFException}) or if an I/O error
     *                     occurs
     */
    public static void readVersionFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        try {
            Version.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new Socks5Exception(e);
        }
    }

}
