package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;

/**
 * An implementation of {@code BytesPropertySpec} of type {@code Bytes}.
 */
public final class BytesPropertySpec extends PropertySpec<Bytes> {

    /**
     * Constructs a {@code BytesPropertySpec} with the provided name and the
     * provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public BytesPropertySpec(final String n, final Bytes defaultVal) {
        super(n, Bytes.class, defaultVal);
    }

    @Override
    protected Bytes parse(final String value) {
        return Bytes.newInstanceFrom(value);
    }

}
