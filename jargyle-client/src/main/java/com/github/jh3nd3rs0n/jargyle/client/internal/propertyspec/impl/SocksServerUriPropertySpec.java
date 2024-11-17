package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;

public final class SocksServerUriPropertySpec
        extends PropertySpec<SocksServerUri> {

    public SocksServerUriPropertySpec(
            final String n, final SocksServerUri defaultVal) {
        super(n, SocksServerUri.class, defaultVal);
    }

    @Override
    protected SocksServerUri parse(final String value) {
        return SocksServerUri.newInstanceFrom(value);
    }

}
