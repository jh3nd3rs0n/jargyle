package com.github.jh3nd3rs0n.jargyle.client;

import java.util.*;

/**
 * A collection of {@code PropertySpec}s.
 */
final class PropertySpecs {

    /**
     * The {@code List} of {@code PropertySpec}s.
     */
    private final List<PropertySpec<Object>> propertySpecs;

    /**
     * The {@code Map} of {@code PropertySpec}s each associated by the name
     * they specify for their {@code Property}.
     */
    private final Map<String, PropertySpec<Object>> propertySpecsMap;

    /**
     * Constructs a {@code PropertySpecs}.
     */
    public PropertySpecs() {
        this.propertySpecs = new ArrayList<>();
        this.propertySpecsMap = new HashMap<>();
    }

    /**
     * Adds the provided {@code PropertySpec} of the specified value type to
     * this {@code PropertySpecs} and then returns the added
     * {@code PropertySpec}.
     *
     * @param value the provided {@code PropertySpec} of the specified value
     *              type
     * @param <T>   the specified value type
     * @return the added {@code PropertySpec}
     */
    public <T> PropertySpec<T> addThenGet(final PropertySpec<T> value) {
        @SuppressWarnings("unchecked")
        PropertySpec<Object> val = (PropertySpec<Object>) value;
        this.propertySpecs.add(val);
        this.propertySpecsMap.put(val.getName(), val);
        return value;
    }

    /**
     * Returns an unmodifiable {@code List} of {@code PropertySpec}s.
     *
     * @return an unmodifiable {@code List} of {@code PropertySpec}s
     */
    public List<PropertySpec<Object>> toList() {
        return Collections.unmodifiableList(this.propertySpecs);
    }

    /**
     * Returns an unmodifiable {@code Map} of {@code PropertySpec}s each
     * associated by the name they specify for their {@code Property}.
     *
     * @return an unmodifiable {@code Map} of {@code PropertySpec}s each
     * associated by the name they specify for their {@code Property}
     */
    public Map<String, PropertySpec<Object>> toMap() {
        return Collections.unmodifiableMap(this.propertySpecsMap);
    }

}
