package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;

public final class BytesPropertySpec extends PropertySpec<Bytes> {

    public BytesPropertySpec(final String n, final Bytes defaultVal) {
        super(n, Bytes.class, defaultVal);
    }

    @Override
    protected Bytes parse(final String value) {
        return Bytes.newInstanceFrom(value);
    }

}
