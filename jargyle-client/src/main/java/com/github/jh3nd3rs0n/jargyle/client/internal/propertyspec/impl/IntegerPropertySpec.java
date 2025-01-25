package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

/**
 * An implementation of {@code PropertySpec} of type {@code Integer}.
 */
public final class IntegerPropertySpec extends PropertySpec<Integer> {

    /**
     * Constructs an {@code IntegerPropertySpec} with the provided name and
     * the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public IntegerPropertySpec(final String n, final Integer defaultVal) {
        super(n, Integer.class, defaultVal);
    }

    @Override
    protected Integer parse(final String value) {
        return Integer.valueOf(value);
    }

}
