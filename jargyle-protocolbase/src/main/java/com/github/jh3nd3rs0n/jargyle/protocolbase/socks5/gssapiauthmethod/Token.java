package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.internal.io.InputStreamHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedShortIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A token of {@code byte}s.
 */
public final class Token {

    /**
     * The maximum length of {@code byte}s of a {@code Token}.
     */
    public static final int MAX_LENGTH = 65535;

    /**
     * The {@code byte}s of this {@code Token}.
     */
    private final byte[] bytes;

    /**
     * Constructs a {@code Token} with the provided {@code byte}s.
     *
     * @param b the provided {@code byte}s
     */
    private Token(final byte[] b) {
        this.bytes = Arrays.copyOf(b, b.length);
    }

    /**
     * Returns a new {@code Token} with the provided {@code byte}s. An
     * {@code IllegalArgumentException} is thrown if the length of the
     * provided {@code byte}s is greater than the maximum length of
     * {@code byte}s for a {@code Token}.
     *
     * @param b the provided {@code byte}s
     * @return a new {@code Token} with the provided {@code byte}s
     */
    public static Token newInstance(final byte[] b) {
        if (b.length > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "byte array must be no more than %s bytes",
                    MAX_LENGTH));
        }
        return new Token(b);
    }

    /**
     * Returns a new {@code Token} from the provided {@code InputStream}. An
     * {@code EOFException} is thrown if the end of the provided
     * {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code Token}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    static Token newInstanceFrom(final InputStream in) throws IOException {
        UnsignedShort len = UnsignedShortIoHelper.readUnsignedShortFrom(
                in);
        byte[] bytes = new byte[len.intValue()];
        if (len.intValue() > 0) {
            int bytesRead = InputStreamHelper.continuouslyReadFrom(in, bytes);
            if (len.intValue() != bytesRead) {
                throw new Socks5Exception(String.format(
                        "expected token length is %s byte(s). "
                                + "actual token length is %s byte(s)",
                        len.intValue(),
                        (bytesRead == -1) ? 0 : bytesRead));
            }
        }
        return Token.newInstance(bytes);
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
        Token other = (Token) obj;
        return Arrays.equals(this.bytes, other.bytes);
    }

    /**
     * Returns the {@code byte}s of this {@code Token}.
     *
     * @return the {@code byte}s of this {@code Token}
     */
    public byte[] getBytes() {
        return Arrays.copyOf(this.bytes, this.bytes.length);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.bytes);
        return result;
    }

    /**
     * Returns the length of {@code byte}s of this {@code Token}.
     *
     * @return the length of {@code byte}s of this {@code Token}
     */
    public int length() {
        return this.bytes.length;
    }

    /**
     * Returns the {@code byte} array of 2 bytes of the length of {@code byte}s
     * of this {@code Token} followed by the {@code byte}s of this
     * {@code Token}.
     *
     * @return the {@code byte} array of 2 bytes of the length of {@code byte}s
     * of this {@code Token} followed by the {@code byte}s of this
     * {@code Token}
     */
    public byte[] toByteArray() {
        byte[] bytesLength = UnsignedShortIoHelper.toByteArray(
                UnsignedShort.valueOf(this.bytes.length));
        byte[] arr = new byte[bytesLength.length + this.bytes.length];
        System.arraycopy(bytesLength, 0, arr, 0, bytesLength.length);
        System.arraycopy(
                this.bytes,
                0,
                arr,
                bytesLength.length,
                this.bytes.length);
        return arr;
    }

    /**
     * Returns the {@code String} representation of this {@code Token}.
     *
     * @return the {@code String} representation of this {@code Token}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [length=" +
                this.length() +
                "]";
    }

}
