package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.text.Values;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class ValuesSettingSpec extends SettingSpec<Values> {

	public ValuesSettingSpec(final String n, final Values defaultVal) {
		super(n, Values.class, defaultVal);
	}

	@Override
	public Setting<Values> newSettingOfParsableValue(final String value) {
		return super.newSetting(Values.newInstance(value));
	}

}
