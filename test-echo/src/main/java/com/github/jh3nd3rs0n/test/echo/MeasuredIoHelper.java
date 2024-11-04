package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.jargyle.internal.io.InputStreamHelper;

import java.io.*;

/**
 * Helper class for reading and writing measured I/O for testing. Measured
 * data as input or output allows for the original data of variable length to
 * be read fully.
 * <p>
 * Measured data is organized in groups of bytes. Each group of bytes is
 * called a chunk. Each chunk begins with a byte called the chunk length byte.
 * If the value of the chuck length byte is between 0 and 254 (inclusive),
 * then that value indicates the number of bytes after the chunk length byte
 * with no further chunks to be followed. If the value of the chunk length
 * byte is 255, then that value indicates 254 bytes after the chunk length
 * byte and another chunk to be followed.
 * </p>
 * <p>
 * Original data is decoded from the measured data by aggregating all of the
 * bytes that are not the chunk length bytes from the chunks of the measured
 * data.
 * </p>
 * <p>
 * Original data of a length of 254 bytes or less is encoded as measured data
 * of only one chunk with a chunk length byte of the value of the length of
 * the bytes of the original data followed by the bytes of the original data.
 * </p>
 * <p>
 * Original data of a length greater than 254 bytes is encoded as measured
 * data of many chunks whose number is the length of the bytes of the original
 * data divided by 254 and rounded up to the next whole number if there is a
 * remainder. Each chunk, with the exception of the last chunk, has a chunk
 * length byte of a value of 255 followed by the next 254 bytes of the
 * original data. The last chunk has a chunk length byte of the value of the
 * length of the remaining bytes of the original data followed by the
 * remaining bytes of the original data.
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
                throw new EOFException("unexpected end of the input stream");
            }
            int bytesToRead = (b < MAX_BYTES_TO_READ + 1) ? b : b - 1;
            byte[] bytes = new byte[bytesToRead];
            if (bytesToRead > 0) {
                int bytesRead = InputStreamHelper.continuouslyReadFrom(
                        in, bytes);
                if (bytesToRead != bytesRead) {
                    throw new IOException(String.format(
                            "expected bytes to read is %s byte(s). "
                                    + "actual bytes read is %s byte(s)",
                            bytesToRead,
                            (bytesRead == -1) ? 0 : bytesRead));
                }
            }
            bytesOut.write(bytes);
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
        out.write(bytesToRead);
        out.write(bytesOut.toByteArray());
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
