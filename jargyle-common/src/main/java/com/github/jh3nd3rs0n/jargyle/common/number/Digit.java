package com.github.jh3nd3rs0n.jargyle.common.number;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

/**
 * An integer between 0 and 9 (inclusive).
 */
@SingleValueTypeDoc(
        description = "An integer between 0 and 9 (inclusive)",
        name = "Digit",
        syntax = "0-9",
        syntaxName = "DIGIT"
)
public final class Digit {

    /**
     * The maximum {@code int} value for a digit.
     */
    public static final int MAX_INT_VALUE = 9;

    /**
     * The minimum {@code int} value for a digit.
     */
    public static final int MIN_INT_VALUE = 0;

    /**
     * The {@code int} value of this {@code Digit}.
     */
    private final int intValue;

    /**
     * Constructs a {@code Digit} of the provided {@code int} value.
     *
     * @param i the provided {@code int} value
     */
    private Digit(final int i) {
        this.intValue = i;
    }

    /**
     * Returns a {@code Digit} of the provided {@code int} value. An
     * {@code IllegalArgumentException} is thrown if the provided {@code int}
     * value is less than 0 or greater than 9.
     *
     * @param i the provided {@code int} value
     * @return a {@code Digit} of the provided {@code int} value
     */
    public static Digit valueOf(final int i) {
        if (i < MIN_INT_VALUE || i > MAX_INT_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "expected an integer between %s and %s (inclusive). "
                            + "actual value is %s",
                    MIN_INT_VALUE,
                    MAX_INT_VALUE,
                    i));
        }
        return new Digit(i);
    }

    /**
     * Returns a {@code Digit} of the provided {@code String}. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is not an integer between 0 and 9 (inclusive).
     *
     * @param s the provided {@code String}
     * @return a {@code Digit} of the provided {@code String}
     */
    public static Digit valueOf(final String s) {
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
        Digit other = (Digit) obj;
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
     * Returns the {@code int} value of this {@code Digit}.
     *
     * @return the {@code int} value of this {@code Digit}
     */
    public int intValue() {
        return this.intValue;
    }

    /**
     * Returns the {@code String} representation of this {@code Digit}. The
     * {@code String} representation would be an integer between 0 and 9
     * (inclusive).
     *
     * @return the {@code String} representation of this {@code Digit}
     */
    @Override
    public String toString() {
        return Integer.toString(this.intValue);
    }

}
