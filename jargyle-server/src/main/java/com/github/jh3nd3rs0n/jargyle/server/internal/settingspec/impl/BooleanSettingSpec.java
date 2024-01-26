package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class BooleanSettingSpec extends SettingSpec<Boolean> {

	public BooleanSettingSpec(final String n, final Boolean defaultVal) {
		super(n, Boolean.class, defaultVal);
	}

	@Override
	protected Boolean parse(final String value) {
		return Boolean.valueOf(value);
	}

}
