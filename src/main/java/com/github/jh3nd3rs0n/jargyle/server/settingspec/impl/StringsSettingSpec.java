package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.text.Strings;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class StringsSettingSpec extends SettingSpec<Strings> {

	public StringsSettingSpec(
			final Object permission, 
			final String s, 
			final Strings defaultVal) {
		super(permission, s, Strings.class, defaultVal);
	}

	@Override
	public Setting<Strings> newSettingOfParsableValue(final String value) {
		return super.newSetting(Strings.newInstance(value));
	}

}
