package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class CommaSeparatedValuesSettingSpec 
    extends SettingSpec<CommaSeparatedValues> {

	public CommaSeparatedValuesSettingSpec(
			final String n, final CommaSeparatedValues defaultVal) {
		super(n, CommaSeparatedValues.class, defaultVal);
	}

	@Override
	public Setting<CommaSeparatedValues> newSettingWithParsedValue(
			final String value) {
		return super.newSetting(CommaSeparatedValues.newInstanceOf(value));
	}

}
