package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;

/**
 * An implementation of {@code PropertySpec} of type
 * {@code NonNegativeInteger}.
 */
public final class NonNegativeIntegerPropertySpec
        extends PropertySpec<NonNegativeInteger> {

    /**
     * Constructs a {@code NonNegativeInteger} with the provided name and the
     * provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public NonNegativeIntegerPropertySpec(
            final String n, final NonNegativeInteger defaultVal) {
        super(n, NonNegativeInteger.class, defaultVal);
    }

    @Override
    protected NonNegativeInteger parse(final String value) {
        return NonNegativeInteger.valueOf(value);
    }

}
