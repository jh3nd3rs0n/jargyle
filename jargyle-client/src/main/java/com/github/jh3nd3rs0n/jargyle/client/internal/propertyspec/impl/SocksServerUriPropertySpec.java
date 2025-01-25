package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;

/**
 * An implementation of {@code PropertySpec} of type {@code SocksServerUri}.
 */
public final class SocksServerUriPropertySpec
        extends PropertySpec<SocksServerUri> {

    /**
     * Constructs a {@code SocksServerUriPropertySpec} with the provided name
     * and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public SocksServerUriPropertySpec(
            final String n, final SocksServerUri defaultVal) {
        super(n, SocksServerUri.class, defaultVal);
    }

    @Override
    protected SocksServerUri parse(final String value) {
        return SocksServerUri.newInstanceFrom(value);
    }

}
