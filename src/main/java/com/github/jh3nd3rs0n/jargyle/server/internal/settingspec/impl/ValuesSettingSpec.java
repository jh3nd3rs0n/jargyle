package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.text.Values;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class ValuesSettingSpec extends SettingSpec<Values> {

	public ValuesSettingSpec(final String s,	final Values defaultVal) {
		super(s, Values.class, defaultVal);
	}

	@Override
	public Setting<Values> newSettingOfParsableValue(final String value) {
		return super.newSetting(Values.newInstance(value));
	}

}
