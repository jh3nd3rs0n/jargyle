package com.github.jh3nd3rs0n.jargyle.common.number;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

/**
 * An integer between 0 and 2147483647 (inclusive).
 */
@SingleValueTypeDoc(
        description = "An integer between 0 and 2147483647 (inclusive)",
        name = "Non-negative Integer",
        syntax = "0-2147483647",
        syntaxName = "NON_NEGATIVE_INTEGER"
)
public final class NonNegativeInteger {

    /**
     * The maximum {@code int} value for a non-negative integer.
     */
    public static final int MAX_INT_VALUE = Integer.MAX_VALUE;

    /**
     * The minimum {@code int} value for a non-negative integer.
     */
    public static final int MIN_INT_VALUE = 0;

    /**
     * The {@code int} value of this {@code NonNegativeInteger}.
     */
    private final int intValue;

    /**
     * Constructs a {@code NonNegativeInteger} of the provided {@code int}
     * value.
     *
     * @param i the provided {@code int} value
     */
    private NonNegativeInteger(final int i) {
        this.intValue = i;
    }

    /**
     * Returns a {@code NonNegativeInteger} of the provided {@code int}
     * value. An {@code IllegalArgumentException} is thrown if the provided
     * {@code int} value is less than 0.
     *
     * @param i the provided {@code int} value
     * @return a {@code NonNegativeInteger} of the provided {@code int}
     * value
     */
    public static NonNegativeInteger valueOf(final int i) {
        if (i < MIN_INT_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "expected an integer between %s and %s (inclusive). "
                            + "actual value is %s",
                    MIN_INT_VALUE,
                    MAX_INT_VALUE,
                    i));
        }
        return new NonNegativeInteger(i);
    }

    /**
     * Returns a {@code NonNegativeInteger} of the provided {@code String}.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is not an integer between 0 and 2147483647 (inclusive).
     *
     * @param s the provided {@code String}
     * @return a {@code NonNegativeInteger} of the provided {@code String}
     */
    public static NonNegativeInteger valueOf(final String s) {
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
        NonNegativeInteger other = (NonNegativeInteger) obj;
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
     * Returns the {@code int} value of this {@code NonNegativeInteger}.
     *
     * @return the {@code int} value of this {@code NonNegativeInteger}
     */
    public int intValue() {
        return this.intValue;
    }

    /**
     * Returns the {@code String} representation of this
     * {@code NonNegativeInteger}. The {@code String} representation is an
     * integer between 0 and 2147483647 (inclusive).
     *
     * @return the {@code String} representation of this
     * {@code NonNegativeInteger}
     */
    @Override
    public String toString() {
        return Integer.toString(this.intValue);
    }

}
