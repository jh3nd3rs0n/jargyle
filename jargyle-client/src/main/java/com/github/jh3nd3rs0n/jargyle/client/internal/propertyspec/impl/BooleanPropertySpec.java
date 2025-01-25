package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

/**
 * An implementation of {@code PropertySpec} of type {@code Boolean}.
 */
public final class BooleanPropertySpec extends PropertySpec<Boolean> {

    /**
     * Constructs a {@code BooleanPropertySpec} with the provided name and the
     * provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public BooleanPropertySpec(final String n, final Boolean defaultVal) {
        super(n, Boolean.class, defaultVal);
    }

    @Override
    protected Boolean parse(final String value) {
        return Boolean.valueOf(value);
    }

}
