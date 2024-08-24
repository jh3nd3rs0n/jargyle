package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortIoHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for reading and writing {@code Port}s.
 */
final class PortIoHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private PortIoHelper() {
    }

    /**
     * Reads a {@code Port} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the provided {@code InputStream} is
     * reached.
     *
     * @param in the provided {@code InputStream}
     * @return a {@code Port}
     * @throws IOException if the end of the provided {@code InputStream} is
     *                     reached ({@code EOFException}) or if an I/O error
     *                     occurs
     */
    public static Port readPortFrom(final InputStream in) throws IOException {
        UnsignedShort s = UnsignedShortIoHelper.readUnsignedShortFrom(in);
        return Port.valueOf(s);
    }

    /**
     * Converts the provided {@code Port} to a {@code byte} array.
     *
     * @param port the provided {@code Port}
     * @return a {@code byte} array
     */
    public static byte[] toByteArray(final Port port) {
        return UnsignedShortIoHelper.toByteArray(port.unsignedShortValue());
    }

}
