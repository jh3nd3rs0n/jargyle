package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class StringSettingSpec extends SettingSpec<String> {

	public StringSettingSpec(final String n, final String defaultVal) {
		super(n, String.class, defaultVal);
	}

	@Override
	public Setting<String> newSettingWithParsedValue(final String value) {
		return super.newSetting(value);
	}
	
}
