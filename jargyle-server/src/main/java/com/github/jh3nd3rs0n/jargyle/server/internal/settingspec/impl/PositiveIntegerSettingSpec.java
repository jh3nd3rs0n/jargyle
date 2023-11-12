package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class PositiveIntegerSettingSpec 
	extends SettingSpec<PositiveInteger> {

	public PositiveIntegerSettingSpec(
			final String n, final PositiveInteger defaultVal) {
		super(n, PositiveInteger.class, defaultVal);
	}

	@Override
	public Setting<PositiveInteger> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(PositiveInteger.newInstance(value));
	}
	
}
