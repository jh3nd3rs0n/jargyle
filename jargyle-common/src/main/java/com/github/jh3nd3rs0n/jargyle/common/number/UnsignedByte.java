package com.github.jh3nd3rs0n.jargyle.common.number;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

/**
 * An integer between 0 and 255 (inclusive).
 */
@SingleValueTypeDoc(
        description = "An integer between 0 and 255 (inclusive)",
        name = "Unsigned Byte",
        syntax = "0-255",
        syntaxName = "UNSIGNED_BYTE"
)
public final class UnsignedByte {

    /**
     * The maximum {@code int} value of an unsigned byte.
     */
    public static final int MAX_INT_VALUE = 0xff;

    /**
     * The minimum {@code int} value of an unsigned byte.
     */
    public static final int MIN_INT_VALUE = 0;

    /**
     * The {@code int} value of this {@code UnsignedByte}.
     */
    private final int intValue;

    /**
     * Constructs an {@code UnsignedByte} of the provided {@code int} value.
     *
     * @param i the provided {@code int} value
     */
    private UnsignedByte(final int i) {
        this.intValue = i;
    }

    /**
     * Returns a {@code UnsignedByte} of the provided {@code byte} value.
     *
     * @param b the provided {@code byte} value
     * @return a {@code UnsignedByte} of the provided {@code byte} value
     */
    public static UnsignedByte valueOf(final byte b) {
        return valueOf(b & MAX_INT_VALUE);
    }

    /**
     * Returns a {@code UnsignedByte} of the provided {@code int} value.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code int} value is less than 0 or greater than 255.
     *
     * @param i the provided {@code int} value
     * @return a {@code UnsignedByte} of the provided {@code int} value
     */
    public static UnsignedByte valueOf(final int i) {
        if (i < MIN_INT_VALUE || i > MAX_INT_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "expected an integer between %s and %s (inclusive). "
                            + "actual value is %s",
                    MIN_INT_VALUE,
                    MAX_INT_VALUE,
                    i));
        }
        return new UnsignedByte(i);
    }

    /**
     * Returns a {@code UnsignedByte} of the provided {@code String}. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is not an integer between 0 and 255 (inclusive).
     *
     * @param s the provided {@code String}
     * @return a {@code UnsignedByte} of the provided {@code String}
     */
    public static UnsignedByte valueOf(final String s) {
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

    /**
     * Returns the {@code byte} value of this {@code UnsignedByte}.
     *
     * @return the {@code byte} value of this {@code UnsignedByte}
     */
    public byte byteValue() {
        return (byte) this.intValue;
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
        UnsignedByte other = (UnsignedByte) obj;
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
     * Returns the {@code int} value of this {@code UnsignedByte}.
     *
     * @return the {@code int} value of this {@code UnsignedByte}
     */
    public int intValue() {
        return this.intValue;
    }

    /**
     * Returns the {@code String} representation of this {@code UnsignedByte}.
     * The {@code String} representation is an integer between 0 and 255
     * (inclusive).
     *
     * @return the {@code String} representation of this {@code UnsignedByte}
     */
    @Override
    public String toString() {
        return Integer.toString(this.intValue);
    }

}
