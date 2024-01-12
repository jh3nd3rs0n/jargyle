package com.github.jh3nd3rs0n.jargyle.common.number;

import java.nio.ByteBuffer;

/**
 * An integer between 0 and 65535 (inclusive).
 */
public final class UnsignedShort {

    /**
     * The length of a byte array of an unsigned short.
     */
    public static final int BYTE_ARRAY_LENGTH = 2;

    /**
     * The maximum {@code int} value of an unsigned short.
     */
    public static final int MAX_INT_VALUE = 0xffff;

    /**
     * The minimum {@code int} value of an unsigned short.
     */
    public static final int MIN_INT_VALUE = 0x0000;

    /**
     * The {@code int} value of this {@code UnsignedShort}.
     */
    private final int intValue;

    /**
     * Constructs an {@code UnsignedShort} of the provided {@code int} value.
     *
     * @param i the provided {@code int} value
     */
    private UnsignedShort(final int i) {
        this.intValue = i;
    }

    /**
     * Returns a {@code UnsignedShort} of the provided byte array. An
     * {@code IllegalArgumentException} is thrown if the length of the
     * provided byte array is not 2.
     *
     * @param b the provided byte array
     * @return a {@code UnsignedShort} of the provided byte array
     */
    public static UnsignedShort valueOf(final byte[] b) {
        if (b.length != BYTE_ARRAY_LENGTH) {
            throw new IllegalArgumentException(String.format(
                    "expected a byte array of a length of %s. "
                            + "actual byte array length is %s",
                    BYTE_ARRAY_LENGTH,
                    b.length));
        }
        ByteBuffer bb = ByteBuffer.wrap(new byte[]{b[0], b[1]});
        return valueOf(bb.getShort() & MAX_INT_VALUE);
    }

    /**
     * Returns a {@code UnsignedShort} of the provided {@code int} value.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code int} value is less than 0 or greater than 65535.
     *
     * @param i the provided {@code int} value
     * @return a {@code UnsignedShort} of the provided {@code int} value
     */
    public static UnsignedShort valueOf(final int i) {
        if (i < MIN_INT_VALUE || i > MAX_INT_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "expected an integer between %s and %s (inclusive). "
                            + "actual value is %s",
                    MIN_INT_VALUE,
                    MAX_INT_VALUE,
                    i));
        }
        return new UnsignedShort(i);
    }

    /**
     * Returns a {@code UnsignedShort} of the provided {@code String}. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is not an integer between 0 and 65535 (inclusive).
     *
     * @param s the provided {@code String}
     * @return a {@code UnsignedShort} of the provided {@code String}
     */
    public static UnsignedShort valueOf(final String s) {
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(
                    "expected an integer between %s and %s (inclusive). "
                            + "actual value is %s",
                    MIN_INT_VALUE,
                    MAX_INT_VALUE,
                    s),
                    e);
        }
        return valueOf(i);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        UnsignedShort other = (UnsignedShort) obj;
        return this.intValue == other.intValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.intValue;
        return result;
    }

    /**
     * Returns the {@code int} value of this {@code UnsignedShort}.
     *
     * @return the {@code int} value of this {@code UnsignedShort}
     */
    public int intValue() {
        return this.intValue;
    }

    /**
     * Returns the byte array of this {@code UnsignedShort}.
     *
     * @return the byte array of this {@code UnsignedShort}
     */
    public byte[] toByteArray() {
        ByteBuffer bb = ByteBuffer.allocate(BYTE_ARRAY_LENGTH);
        bb.putShort((short) this.intValue);
        return bb.array();
    }

    /**
     * Returns the {@code String} representation of this {@code UnsignedShort}.
     * The {@code String} representation is an integer between 0 and 65535
     * (inclusive).
     *
     * @return the {@code String} representation of this {@code UnsignedShort}
     */
    @Override
    public String toString() {
        return Integer.toString(this.intValue);
    }

}
