package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;

public final class HostAddressTypesPropertySpec
        extends PropertySpec<HostAddressTypes> {

    public HostAddressTypesPropertySpec(
            final String n, final HostAddressTypes defaultVal) {
        super(n, HostAddressTypes.class, defaultVal);
    }

    @Override
    protected HostAddressTypes parse(final String value) {
        return HostAddressTypes.newInstanceFrom(value);
    }

}
