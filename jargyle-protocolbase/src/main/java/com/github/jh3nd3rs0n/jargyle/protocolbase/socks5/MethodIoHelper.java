package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for reading and writing {@code Method}s.
 */
final class MethodIoHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private MethodIoHelper() {
    }

    /**
     * Reads a {@code Method} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a {@code Method}
     * @throws IOException if the end of the provided {@code InputStream} is
     *                     reached ({@code EOFException}) or if an I/O error
     *                     occurs
     */
    public static Method readMethodFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        Method method;
        try {
            method = Method.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new Socks5Exception(e);
        }
        return method;
    }

}
