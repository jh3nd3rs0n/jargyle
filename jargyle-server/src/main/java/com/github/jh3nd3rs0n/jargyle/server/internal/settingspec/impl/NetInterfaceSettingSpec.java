package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class NetInterfaceSettingSpec extends SettingSpec<NetInterface> {

    public NetInterfaceSettingSpec(
            final String n, final NetInterface defaultVal) {
        super(n, NetInterface.class, defaultVal);
    }

    @Override
    protected NetInterface parse(final String value) {
        return NetInterface.newInstanceFrom(value);
    }

}
