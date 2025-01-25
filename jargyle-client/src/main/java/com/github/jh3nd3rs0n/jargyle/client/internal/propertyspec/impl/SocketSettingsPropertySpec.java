package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;

/**
 * An implementation of {@code PropertySpec} of type {@code SocketSettings}.
 */
public final class SocketSettingsPropertySpec
        extends PropertySpec<SocketSettings> {

    /**
     * Constructs a {@code SocketSettingsPropertySpec} with the provided name
     * and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public SocketSettingsPropertySpec(
            final String n, final SocketSettings defaultVal) {
        super(n, SocketSettings.class, defaultVal);
    }

    @Override
    protected SocketSettings parse(final String value) {
        return SocketSettings.newInstanceFrom(value);
    }

}
