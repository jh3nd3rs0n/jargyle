package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public class IntegerSettingSpec extends SettingSpec<Integer> {

    public IntegerSettingSpec(final String n, Integer defaultVal) {
        super(n, Integer.class, defaultVal);
    }

    @Override
    protected Integer parse(final String value) {
        return Integer.valueOf(value);
    }

}
