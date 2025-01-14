package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class BytesSettingSpec extends SettingSpec<Bytes> {

    public BytesSettingSpec(final String n, final Bytes defaultVal) {
        super(n, Bytes.class, defaultVal);
    }

    @Override
    protected Bytes parse(final String value) {
        return Bytes.newInstanceFrom(value);
    }

}
