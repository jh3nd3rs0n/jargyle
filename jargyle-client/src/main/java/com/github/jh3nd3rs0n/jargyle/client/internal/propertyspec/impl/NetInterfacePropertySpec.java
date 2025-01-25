package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;

/**
 * An implementation of {@code PropertySpec} of type {@code NetInterface}.
 */
public final class NetInterfacePropertySpec
        extends PropertySpec<NetInterface> {

    /**
     * Constructs a {@code NetInterfacePropertySpec} with the provided name
     * and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public NetInterfacePropertySpec(
            final String n, final NetInterface defaultVal) {
        super(n, NetInterface.class, defaultVal);
    }

    @Override
    protected NetInterface parse(final String value) {
        return NetInterface.newInstanceFrom(value);
    }

}
