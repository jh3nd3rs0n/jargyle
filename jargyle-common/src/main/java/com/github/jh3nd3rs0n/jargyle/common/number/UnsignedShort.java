package com.github.jh3nd3rs0n.jargyle.common.number;

/**
 * An integer between 0 and 65535 (inclusive).
 */
public final class UnsignedShort {

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
     * Returns a {@code UnsignedShort} of the provided {@code short} value.
     *
     * @param s the provided {@code short} value
     * @return a {@code UnsignedShort} of the provided {@code short} value
     */
    public static UnsignedShort valueOf(final short s) {
        return valueOf(s & MAX_INT_VALUE);
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
     * Returns the {@code short} value of this {@code UnsignedShort}.
     *
     * @return the {@code short} value of this {@code UnsignedShort}
     */
    public short shortValue() {
        return (short) this.intValue;
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
