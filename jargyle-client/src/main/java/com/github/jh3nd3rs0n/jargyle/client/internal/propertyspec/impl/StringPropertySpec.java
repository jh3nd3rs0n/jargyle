package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

/**
 * An implementation of {@code PropertySpec} of type {@code String}.
 */
public final class StringPropertySpec extends PropertySpec<String> {

    /**
     * Constructs a {@code StringPropertySpec} with the provided name and the
     * provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public StringPropertySpec(final String n, final String defaultVal) {
        super(n, String.class, defaultVal);
    }

    @Override
    protected String parse(final String value) {
        return value;
    }

}
