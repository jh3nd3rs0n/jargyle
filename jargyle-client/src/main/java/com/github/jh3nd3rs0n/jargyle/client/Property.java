package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;

/**
 * A property of the specified value type.
 *
 * @param <V> the specified value type
 */
@NameValuePairValueTypeDoc(
        description = "A property",
        name = "Property",
        nameValuePairValueSpecs = {
                GeneralPropertySpecConstants.class,
                DtlsPropertySpecConstants.class,
                Socks5PropertySpecConstants.class,
                SslPropertySpecConstants.class
        },
        syntax = "NAME=VALUE",
        syntaxName = "PROPERTY"
)
public final class Property<V> {

    /**
     * The name of this {@code Property}.
     */
    private final String name;

    /**
     * The {@code PropertySpec} that defines this {@code Property}.
     */
    private final PropertySpec<V> propertySpec;

    /**
     * The value of this {@code Property}.
     */
    private final V value;

    /**
     * Constructs a {@code Property} with the provided {@code PropertySpec}
     * and the provided value.
     *
     * @param spec the provided {@code PropertySpec}
     * @param val  the provided value
     */
    Property(final PropertySpec<V> spec, final V val) {
        V v = spec.getValueType().cast(val);
        this.name = spec.getName();
        this.propertySpec = spec;
        this.value = v;
    }

    /**
     * Returns a new {@code Property} of the specified value type with the
     * provided name specified by a {@code PropertySpec} and the provided
     * value. An {@code IllegalArgumentException} is thrown if the provided
     * name is not specified by any {@code PropertySpec}. A
     * {@code ClassCastException} is thrown if the provided value is not
     * assignable to the value type defined by the {@code PropertySpec}.
     *
     * @param name  the provided name specified by a {@code PropertySpec}
     * @param value the provided value (can be {@code null})
     * @param <V>   the specified value type
     * @return a new {@code Property} of the specified value type with the
     * provided name specified by a {@code PropertySpec} and the provided
     * value
     */
    public static <V> Property<V> newInstance(
            final String name, final V value) {
        PropertySpec<Object> propertySpec = PropertySpecConstants.valueOfName(
                name);
        @SuppressWarnings("unchecked")
        Property<V> property = (Property<V>) propertySpec.newProperty(value);
        return property;
    }

    /**
     * Returns a new {@code Property} of the {@code Object} value type with
     * the provided name specified by a {@code PropertySpec} and with the
     * parsed value from the provided {@code String} value. An
     * {@code IllegalArgumentException} is thrown if the provided name is not
     * specified by any {@code PropertySpec} or the provided {@code String}
     * value to be parsed is invalid.
     *
     * @param name  the provided name specified by a {@code PropertySpec}
     * @param value the provided {@code String} value to be parsed
     * @return a new {@code Property} of the {@code Object} value type with
     * the provided name specified by a {@code PropertySpec} and with the
     * parsed value from the provided {@code String} value
     */
    public static Property<Object> newInstanceWithParsedValue(
            final String name, final String value) {
        PropertySpec<Object> propertySpec = PropertySpecConstants.valueOfName(
                name);
        return propertySpec.newPropertyWithParsedValue(value);
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
        Property<?> other = (Property<?>) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.value == null) {
            return other.value == null;
        }
        return this.value.equals(other.value);
    }

    /**
     * Returns the name of this {@code Property}.
     *
     * @return the name of this {@code Property}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the {@code PropertySpec} that defines this {@code Property}.
     *
     * @return the {@code PropertySpec} that defines this {@code Property}
     */
    public PropertySpec<V> getPropertySpec() {
        return this.propertySpec;
    }

    /**
     * Returns the value of this {@code Property}.
     *
     * @return the value of this {@code Property}
     */
    public V getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.name.hashCode();
        result = prime * result + ((this.value == null) ?
                0 : this.value.hashCode());
        return result;
    }

    /**
     * Returns the {@code String} representation of this {@code Property}. The
     * {@code String} representation is the name of this {@code Property}
     * followed by an equal sign (=) followed by the {@code String}
     * representation of the value of this {@code Property}.
     *
     * @return the {@code String} representation of this {@code Property}
     */
    @Override
    public String toString() {
        return String.format("%s=%s", this.name, this.value);
    }

}
