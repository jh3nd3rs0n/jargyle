package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;

/**
 * An implementation of {@code PropertySpec} of type {@code PositiveInteger}.
 */
public final class PositiveIntegerPropertySpec
        extends PropertySpec<PositiveInteger> {

    /**
     * Constructs a {@code PositiveIntegerPropertySpec} with the provided name
     * and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public PositiveIntegerPropertySpec(
            final String n, final PositiveInteger defaultVal) {
        super(n, PositiveInteger.class, defaultVal);
    }

    @Override
    protected PositiveInteger parse(final String value) {
        return PositiveInteger.valueOf(value);
    }

}
