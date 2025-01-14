package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;

public final class NonNegativeIntegerPropertySpec
        extends PropertySpec<NonNegativeInteger> {

    public NonNegativeIntegerPropertySpec(
            final String n, final NonNegativeInteger defaultVal) {
        super(n, NonNegativeInteger.class, defaultVal);
    }

    @Override
    protected NonNegativeInteger parse(final String value) {
        return NonNegativeInteger.valueOf(value);
    }

}
