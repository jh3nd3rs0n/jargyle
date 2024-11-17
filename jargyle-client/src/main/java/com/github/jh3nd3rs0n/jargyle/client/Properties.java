package com.github.jh3nd3rs0n.jargyle.client;

import java.util.*;

/**
 * A collection of {@code Property}s.
 */
public final class Properties {

    /**
     * The {@code Map} of {@code Property}s each associated by their
     * {@code PropertySpec}.
     */
    private final Map<PropertySpec<Object>, Property<Object>> properties;

    /**
     * Constructs a {@code Properties} of the provided {@code List} of
     * {@code Property}s.
     *
     * @param props the provided {@code List} of {@code Property}s
     */
    private Properties(final List<Property<?>> props) {
        Map<PropertySpec<Object>, Property<Object>> map =
                new LinkedHashMap<>();
        for (Property<?> prop : props) {
            @SuppressWarnings("unchecked")
            PropertySpec<Object> propSpec =
                    (PropertySpec<Object>) prop.getPropertySpec();
            map.remove(propSpec);
            @SuppressWarnings("unchecked")
            Property<Object> prp = (Property<Object>) prop;
            map.put(propSpec, prp);
        }
        this.properties = map;
    }

    /**
     * Constructs a {@code Properties} of another {@code Properties}.
     *
     * @param other the other {@code Properties}
     */
    private Properties(final Properties other) {
        this.properties = new LinkedHashMap<>(other.properties);
    }

    /**
     * Returns a {@code Properties} of the provided {@code List} of
     * {@code Property}s.
     *
     * @param properties the provided {@code List} of {@code Property}s
     * @return a {@code Properties} of the provided {@code List} of
     * {@code Property}s
     */
    public static Properties of(final List<Property<?>> properties) {
        return new Properties(properties);
    }

    /**
     * Returns a {@code Properties} of the provided {@code Property}s.
     *
     * @param properties the provided {@code Property}s
     * @return a {@code Properties} of the provided {@code Property}s
     */
    public static Properties of(final Property<?>... properties) {
        return of(Arrays.asList(properties));
    }

    /**
     * Returns a {@code Properties} of another {@code Properties}.
     *
     * @param other the other {@code Properties}
     * @return a {@code Properties} of another {@code Properties}
     */
    public static Properties of(final Properties other) {
        return new Properties(other);
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
        Properties other = (Properties) obj;
        return this.properties.equals(other.properties);
    }

    /**
     * Returns the value or default value of the {@code Property} defined by
     * the provided {@code PropertySpec} of the specified value type.
     *
     * @param propertySpec the provided {@code PropertySpec}
     * @param <V>          the specified value type
     * @return the value or default value of the {@code Property} defined by
     * the provided {@code PropertySpec} of the specified value type
     */
    public <V> V getValue(final PropertySpec<V> propertySpec) {
        V value = null;
        Property<Object> property = this.properties.get(propertySpec);
        if (property != null) {
            value = propertySpec.getValueType().cast(property.getValue());
        }
        if (value == null) {
            Property<V> defaultProperty = propertySpec.getDefaultProperty();
            value = defaultProperty.getValue();
        }
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.properties.hashCode();
        return result;
    }

    /**
     * Returns an unmodifiable {@code Map} of {@code Property}s each
     * associated by their {@code PropertySpec}.
     *
     * @return an unmodifiable {@code Map} of {@code Property}s each
     * associated by their {@code PropertySpec}
     */
    public Map<PropertySpec<Object>, Property<Object>> toMap() {
        return Collections.unmodifiableMap(this.properties);
    }

    /**
     * Returns the {@code String} representation of this {@code Properties}.
     *
     * @return the {@code String} representation of this {@code Properties}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [properties=" +
                this.properties +
                "]";
    }

}
