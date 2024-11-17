package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

/**
 * The specification of a {@code Property} of the specified value type.
 *
 * @param <V> the specified value type
 */
public abstract class PropertySpec<V> {

    /**
     * The name of the {@code Property}.
     */
    private final String name;

    /**
     * The value type of the {@code Property}.
     */
    private final Class<V> valueType;

    /**
     * The default {@code Property} with the default value.
     */
    private final Property<V> defaultProperty;

    /**
     * Constructs a {@code PropertySpec} with the provided name, the provided
     * value type, and the provided default value.
     *
     * @param n          the provided name
     * @param valType    the provided value type
     * @param defaultVal the provided default value (can be {@code null})
     */
    public PropertySpec(
            final String n, final Class<V> valType, final V defaultVal) {
        Objects.requireNonNull(n);
        Objects.requireNonNull(valType);
        this.name = n;
        this.valueType = valType;
        this.defaultProperty = new Property<>(this, valType.cast(defaultVal));
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
        PropertySpec<?> other = (PropertySpec<?>) obj;
        return this.name.equals(other.name);
    }

    /**
     * Returns the default {@code Property}.
     *
     * @return the default {@code Property}
     */
    public final Property<V> getDefaultProperty() {
        return this.defaultProperty;
    }

    /**
     * Returns the name of the {@code Property}.
     *
     * @return the name of the {@code Property}
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Returns the value type of the {@code Property}.
     *
     * @return the value type of the {@code Property}
     */
    public final Class<V> getValueType() {
        return this.valueType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.name.hashCode();
        return result;
    }

    /**
     * Returns a new {@code Property} with the provided value. An
	 * {@code IllegalArgumentException} is thrown if the provided value is
	 * invalid.
     *
     * @param value the provided value
     * @return a new {@code Property} with the provided value
     */
    public final Property<V> newProperty(final V value) {
        return new Property<>(
                this,
                this.valueType.cast(this.validate(value)));
    }

    /**
     * Returns a new {@code Property} with the parsed value from the provided
     * {@code String} value. An {@code IllegalArgumentException} is thrown if
     * the provided {@code String} value to be parsed is invalid or if the
	 * parsed value is invalid.
     *
     * @param value the provided {@code String} value to be parsed
     * @return a new {@code Property} with the parsed value from the provided
     * {@code String} value
     */
    public final Property<V> newPropertyWithParsedValue(final String value) {
        return this.newProperty(this.parse(value));
    }

    /**
     * Returns the parsed value from the provided {@code String} value. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} value to be parsed is invalid.
     *
     * @param value the {@code String} value to be parsed
     * @return the parsed value from the provided {@code String} value
     */
    protected abstract V parse(final String value);

    /**
     * Returns the {@code String} representation of this {@code PropertySpec}.
     *
     * @return the {@code String} representation of this {@code PropertySpec}
     */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() +
                " [name=" +
                this.name +
                "]";
    }

    /**
     * Returns the valid provided value. Implementations can override this
     * method to throw an {@code IllegalArgumentException} if the provided
     * value is invalid.
     *
     * @param value the provided value
     * @return the valid provided value
     */
    protected V validate(final V value) {
        return value;
    }

}
