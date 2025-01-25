package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;

/**
 * An implementation of {@code PropertySpec} of type {@code Methods}.
 */
public final class Socks5MethodsPropertySpec extends PropertySpec<Methods> {

    /**
     * Constructs a {@code Socks5MethodsPropertySpec} with the provided name
     * and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public Socks5MethodsPropertySpec(
            final String n, final Methods defaultVal) {
        super(n, Methods.class, defaultVal);
    }

    @Override
    protected Methods parse(final String value) {
        return Methods.newInstanceFrom(value);
    }

}
