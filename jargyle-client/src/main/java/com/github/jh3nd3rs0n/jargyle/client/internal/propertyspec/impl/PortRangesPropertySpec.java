package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;

/**
 * An implementation of {@code PropertySpec} of type {@code PortRanges}.
 */
public final class PortRangesPropertySpec extends PropertySpec<PortRanges> {

    /**
     * Constructs a {@code PortRangesPropertySpec} with the provided name
     * and the provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public PortRangesPropertySpec(
            final String n, final PortRanges defaultVal) {
        super(n, PortRanges.class, defaultVal);
    }

    @Override
    protected PortRanges parse(final String value) {
        return PortRanges.newInstanceFrom(value);
    }

}
