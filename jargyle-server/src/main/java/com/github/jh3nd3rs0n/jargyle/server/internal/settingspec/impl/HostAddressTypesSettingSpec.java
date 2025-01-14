package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class HostAddressTypesSettingSpec
        extends SettingSpec<HostAddressTypes> {

    public HostAddressTypesSettingSpec(
            final String n, final HostAddressTypes defaultVal) {
        super(n, HostAddressTypes.class, defaultVal);
    }

    @Override
    protected HostAddressTypes parse(final String value) {
        return HostAddressTypes.newInstanceFrom(value);
    }

}
