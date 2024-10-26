package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.internal.io.InputStreamHelper;

import java.io.*;
import java.util.Arrays;

/**
 * Helper class for reading and writing measured I/O for testing.
 * <p>
 * Measured data starts with the first byte indicating the number of the next
 * bytes to be read. If the value of the first byte is between 0 and 254
 * (inclusive), then that value is the number of the last of the next bytes to
 * be read. If the value of the first byte is 255, then that value indicates
 * that the number of the next bytes to be read is 254 plus another byte to be
 * read and evaluated like the first byte.
 * </p>
 */
final class MeasuredIoHelper {

    /**
     * The maximum number of bytes to read.
     */
    private static final int MAX_BYTES_TO_READ = 254;

    /**
     * Prevents the construction of unnecessary instances.
     */
    private MeasuredIoHelper() {
    }

    /**
     * Returns a {@code byte} array of decoded data read from the provided
     * {@code InputStream} of measured data.
     *
     * @param in the provided {@code InputStream} of measured data
     * @return a {@code byte} array of decoded data read from the provided
     * {@code InputStream} of measured data
     * @throws IOException if an I/O error occurs in reading from the
     *                     provided {@code InputStream}
     */
    public static byte[] readFrom(final InputStream in) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        while (true) {
            int b = in.read();
            if (b == -1) {
                break;
            }
            int bytesToRead = (b < MAX_BYTES_TO_READ + 1) ? b : b - 1;
            byte[] bytes = new byte[bytesToRead];
            int bytesRead = InputStreamHelper.continuouslyReadFrom(in, bytes);
            if (bytesRead == -1) {
                break;
            }
            bytesOut.write(Arrays.copyOf(bytes, bytesRead));
            if (b < MAX_BYTES_TO_READ + 1) {
                break;
            }
        }
        return bytesOut.toByteArray();
    }

    /**
     * Encodes and writes the provided length of bytes from the provided
     * {@code byte} array starting at the provided offset to the provided
     * {@code OutputStream} and then flushes the provided {@code OutputStream}.
     *
     * @param b   the provided {@code byte} array
     * @param off the provided offset
     * @param len the provided length of bytes from the provided {@code byte}
     *            array
     * @param out the provided {@code OutputStream}
     * @throws IOException if an I/O error occurs in writing to the
     *                     {@code OutputStream} or flushing the provided
     *                     {@code OutputStream}
     */
    public static void writeThenFlush(
            final byte[] b,
            final int off,
            final int len,
            final OutputStream out) throws IOException {
        if (len == 0) {
            out.write(len);
            out.flush();
            return;
        }
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        int bytesToRead = 0;
        for (int i = off; i < off + len; i++) {
            bytesOut.write(b[i]);
            if (++bytesToRead == MAX_BYTES_TO_READ && i + 1 < off + len) {
                out.write(MAX_BYTES_TO_READ + 1);
                out.write(bytesOut.toByteArray());
                bytesToRead = 0;
                bytesOut = new ByteArrayOutputStream();
            }
        }
        if (bytesToRead > 0) {
            out.write(bytesToRead);
            out.write(bytesOut.toByteArray());
        }
        out.flush();
    }

    /**
     * Encodes and writes the provided {@code byte} array to the provided
     * {@code OutputStream} and then flushes the provided {@code OutputStream}.
     * <p>
     * It is equivalent to the following code:
     * </p>
     * <p>
     * <pre>writeThenFlush(b, 0, b.length, out);
     * </pre>
     * </p>
     *
     * @param b   the provided {@code byte} array
     * @param out the provided {@code OutputStream}
     * @throws IOException if an I/O error occurs in writing to the provided
     *                     {@code OutputStream} or flushing the provided
	 *                     {@code OutputStream}
     */
    public static void writeThenFlush(
            final byte[] b, final OutputStream out) throws IOException {
        writeThenFlush(b, 0, b.length, out);
    }

}
