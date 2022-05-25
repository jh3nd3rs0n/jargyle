package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class BooleanSettingSpec extends SettingSpec<Boolean> {

	public BooleanSettingSpec(final String s, final Boolean defaultVal) {
		super(s, Boolean.class, defaultVal);
	}

	@Override
	public Setting<Boolean> newSettingOfParsableValue(final String value) {
		return super.newSetting(Boolean.valueOf(value));
	}	

}
