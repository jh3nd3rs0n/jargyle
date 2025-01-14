package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;

public final class NetInterfacePropertySpec
        extends PropertySpec<NetInterface> {

    public NetInterfacePropertySpec(
            final String n, final NetInterface defaultVal) {
        super(n, NetInterface.class, defaultVal);
    }

    @Override
    protected NetInterface parse(final String value) {
        return NetInterface.newInstanceFrom(value);
    }

}
