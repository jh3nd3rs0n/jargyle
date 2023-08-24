package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.lang.Strings;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class StringsSettingSpec extends SettingSpec<Strings> {

	public StringsSettingSpec(final String n, final Strings defaultVal) {
		super(n, Strings.class, defaultVal);
	}

	@Override
	public Setting<Strings> newSettingOfParsableValue(final String value) {
		return super.newSetting(Strings.newInstance(value));
	}

}
