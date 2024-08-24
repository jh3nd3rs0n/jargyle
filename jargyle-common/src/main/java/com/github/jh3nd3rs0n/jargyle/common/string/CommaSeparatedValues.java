package com.github.jh3nd3rs0n.jargyle.common.string;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.util.Arrays;

/**
 * A collection of {@code String} values whose {@code String} representation
 * is a comma separated list of said {@code String} values.
 */
@ValuesValueTypeDoc(
        description = "A comma separated list of values",
        elementValueType = String.class,
        name = "Comma Separated Values",
        syntax = "[VALUE1[,VALUE2[...]]]",
        syntaxName = "COMMA_SEPARATED_VALUES"
)
public final class CommaSeparatedValues {

    /**
     * The values of this {@code CommaSeparatedValues}.
     */
    private final String[] values;

    /**
     * Constructs a {@code CommaSeparatedValues} of the provided values.
     *
     * @param vals the provided values
     */
    private CommaSeparatedValues(final String[] vals) {
        this.values = Arrays.copyOf(vals, vals.length);
    }

    /**
     * Returns a new {@code CommaSeparatedValues} from the provided
     * {@code String}. The provided {@code String} must be a comma separated
     * list of values. The provided {@code String} can also be empty which
     * would result in an empty {@code CommaSeparatedValues}.
     *
     * @param s the provided {@code String}
     * @return a new {@code CommaSeparatedValues} from the provided
     * {@code String}
     */
    public static CommaSeparatedValues newInstanceFrom(final String s) {
        if (s.isEmpty()) {
            return of();
        }
        return of(s.split(",", -1));
    }

    /**
     * Returns a {@code CommaSeparatedValues} of the provided values.
     *
     * @param vals the provided values
     * @return a {@code CommaSeparatedValues} of the provided values
     */
    public static CommaSeparatedValues of(final String... vals) {
        return new CommaSeparatedValues(vals);
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
        CommaSeparatedValues other = (CommaSeparatedValues) obj;
        return Arrays.equals(this.values, other.values);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.values);
        return result;
    }

    /**
     * Returns the values of this {@code CommaSeparatedValues}.
     *
     * @return the values of this {@code CommaSeparatedValues}
     */
    public String[] toArray() {
        return Arrays.copyOf(this.values, this.values.length);
    }

    /**
     * Returns the {@code String} representation of this
     * {@code CommaSeparatedValues}. The {@code String} representation is a
     * comma separated list of the values in this {@code CommaSeparatedValues}.
     *
     * @return the {@code String} representation of this
     * {@code CommaSeparatedValues}
     */
    @Override
    public String toString() {
        return String.join(",", this.values);
    }

}
