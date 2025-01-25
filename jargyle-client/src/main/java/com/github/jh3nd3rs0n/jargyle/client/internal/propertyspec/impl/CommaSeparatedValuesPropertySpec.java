package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;

/**
 * An implementation of {@code PropertySpec} of type
 * {@code CommaSeparatedValues}.
 */
public final class CommaSeparatedValuesPropertySpec
        extends PropertySpec<CommaSeparatedValues> {

    /**
     * Constructs a {@code CommaSeparatedValuesPropertySpec} with the provided
     * name and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public CommaSeparatedValuesPropertySpec(
            final String n, final CommaSeparatedValues defaultVal) {
        super(n, CommaSeparatedValues.class, defaultVal);
    }

    @Override
    protected CommaSeparatedValues parse(final String value) {
        return CommaSeparatedValues.newInstanceFrom(value);
    }

}
