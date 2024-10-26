package com.github.jh3nd3rs0n.jargyle.internal.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for {@code InputStream}s.
 */
public final class InputStreamHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private InputStreamHelper() {
    }

    /**
     * Continuously reads from the provided {@code InputStream} the number of
     * bytes from the length of the provided {@code byte} array into the
     * provided {@code byte} array until the length of the provided
     * {@code byte} array is reached or until the end of the provided
     * {@code InputStream} is reached. The number of bytes actually read is
     * returned as an integer. If no bytes were read and the end of the
     * provided {@code InputStream} is reached, a {@code -1} is returned.
     *
     * @param in the provided {@code InputStream}
     * @param b  the provided {@code byte} array
     * @return the number of bytes actually read or {@code -1} if no bytes
     * were read and the end of the provided {@code InputStream} is reached
     * @throws IOException if an I/O error occurs
     */
    public static int continuouslyReadFrom(
            final InputStream in, final byte[] b) throws IOException {
        int length = b.length;
        int offset = 0;
        int totalBytesRead = -1;
        while (length > 0) {
            int bytesRead = in.read(b, offset, length);
            if (bytesRead == -1) {
                break;
            }
            if (totalBytesRead == -1) {
                totalBytesRead = 0;
            }
            length -= bytesRead;
            offset += bytesRead;
            totalBytesRead += bytesRead;
        }
        return totalBytesRead;
    }

}
