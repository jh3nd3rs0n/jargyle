package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

public class IntegerPropertySpec extends PropertySpec<Integer> {

    public IntegerPropertySpec(final String n, final Integer defaultVal) {
        super(n, Integer.class, defaultVal);
    }

    @Override
    protected Integer parse(final String value) {
        return Integer.valueOf(value);
    }

}
