package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;

/**
 * An implementation of {@code PropertySpec} of type {@code HostAddressTypes}.
 */
public final class HostAddressTypesPropertySpec
        extends PropertySpec<HostAddressTypes> {

    /**
     * Constructs a {@code HostAddressTypesPropertySpec} with the provided
     * name and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public HostAddressTypesPropertySpec(
            final String n, final HostAddressTypes defaultVal) {
        super(n, HostAddressTypes.class, defaultVal);
    }

    @Override
    protected HostAddressTypes parse(final String value) {
        return HostAddressTypes.newInstanceFrom(value);
    }

}
