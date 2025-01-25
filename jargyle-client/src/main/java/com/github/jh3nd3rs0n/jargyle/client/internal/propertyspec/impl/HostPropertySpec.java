package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;

/**
 * An implementation of {@code PropertySpec} of type {@code Host}.
 */
public final class HostPropertySpec extends PropertySpec<Host> {

    /**
     * Constructs a {@code HostPropertySpec} with the provided name and the
     * provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public HostPropertySpec(final String n, final Host defaultVal) {
        super(n, Host.class, defaultVal);
    }

    @Override
    protected Host parse(final String value) {
        return Host.newInstance(value);
    }

}
