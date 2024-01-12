package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class NonnegativeIntegerSettingSpec 
	extends SettingSpec<NonNegativeInteger> {

	public NonnegativeIntegerSettingSpec(
			final String n, final NonNegativeInteger defaultVal) {
		super(n, NonNegativeInteger.class, defaultVal);
	}

	@Override
	public Setting<NonNegativeInteger> newSettingWithParsedValue(
			final String value) {
		return super.newSetting(NonNegativeInteger.valueOf(value));
	}

}
