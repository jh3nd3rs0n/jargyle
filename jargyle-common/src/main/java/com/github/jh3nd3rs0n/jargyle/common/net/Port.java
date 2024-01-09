package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

import java.util.Objects;

/**
 * An integer between 0 and 65535 (inclusive) that is assigned to uniquely
 * identify a connection endpoint and to direct data to a host.
 */
@SingleValueTypeDoc(
        description = "An integer between 0 and 65535 (inclusive) that is " +
                "assigned to uniquely identify a connection endpoint and to " +
                "direct data to a host",
        name = "Port",
        syntax = "0-65535",
        syntaxName = "PORT"
)
public final class Port implements Comparable<Port> {

    /**
     * The maximum {@code int} value for a port number.
     */
    public static final int MAX_INT_VALUE = UnsignedShort.MAX_INT_VALUE;

    /**
     * The minimum {@code int} value for a port number.
     */
    public static final int MIN_INT_VALUE = UnsignedShort.MIN_INT_VALUE;

    /**
     * The {@code UnsignedShort} value of this {@code Port}.
     */
    private final UnsignedShort unsignedShortValue;

    /**
     * Constructs a {@code Port} of the provided {@code UnsignedShort}.
     *
     * @param unsignedShortVal the provided {@code UnsignedShort}
     */
    private Port(final UnsignedShort unsignedShortVal) {
        this.unsignedShortValue = Objects.requireNonNull(unsignedShortVal);
    }

    /**
     * Returns a new {@code Port} of the provided {@code int} value. An
     * {@code IllegalArgumentException} is thrown if the provided {@code int}
     * value is not an {@code int} value between 0 and 65535.
     *
     * @param i the provided {@code int} value
     * @return a new {@code Port} of the provided {@code int} value
     */
    public static Port newInstanceOf(final int i) {
        return new Port(UnsignedShort.newInstanceOf(i));
    }

    /**
     * Returns a new {@code Port} of the provided {@code String}. The
     * provided {@code String} must be an integer between 0 and 65535
     * (inclusive). An {@code IllegalArgumentException} is thrown if the
     * provided {@code String} is not valid.
     *
     * @param s the provided {@code String}
     * @return a new {@code Port} of the provided {@code String}
     */
    public static Port newInstanceOf(final String s) {
        return new Port(UnsignedShort.newInstanceOf(s));
    }

    /**
     * Returns a new {@code Port} of the provided {@code UnsignedShort}.
     *
     * @param s the provided {@code UnsignedShort}
     * @return a new {@code Port} of the provided {@code UnsignedShort}
     */
    public static Port newInstanceOf(final UnsignedShort s) {
        return new Port(s);
    }

    /**
     * Compares this {@code Port} with the other {@code Port}.
     *
     * @param other the other {@code Port} to be compared.
     * @return an {@code int} value to indicate if this {@code Port} is less
     * than the other {@code Port} (negative {@code int} value), if this
     * {@code Port} is equal to the other {@code Port} ({@code int} value of
     * zero), or if this {@code Port} is greater than the other {@code Port}
     * (positive {@code int} value).
     */
    @Override
    public int compareTo(final Port other) {
        return this.unsignedShortValue.intValue() - other.unsignedShortValue.intValue();
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
        Port other = (Port) obj;
        return this.unsignedShortValue.equals(other.unsignedShortValue);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.unsignedShortValue.hashCode();
        return result;
    }

    /**
     * Returns the {@code int} value of this {@code Port}.
     *
     * @return the {@code int} value of this {@code Port}
     */
    public int intValue() {
        return this.unsignedShortValue.intValue();
    }

    /**
     * Returns the {@code String} representation of this {@code Port}. The
     * {@code String} representation is an integer between 0 and 65535.
     *
     * @return the {@code String} representation of this {@code Port}
     */
    public String toString() {
        return this.unsignedShortValue.toString();
    }

    /**
     * Returns the {@code UnsignedShort} value of this {@code Port}.
     *
     * @return the {@code UnsignedShort} value of this {@code Port}
     */
    public UnsignedShort toUnsignedShort() {
        return this.unsignedShortValue;
    }

}
