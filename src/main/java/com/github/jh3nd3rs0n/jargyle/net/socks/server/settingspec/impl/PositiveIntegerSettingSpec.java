package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;

final class PositiveIntegerSettingSpec 
	extends SettingSpec<PositiveInteger> {

	public PositiveIntegerSettingSpec(
			final String s, final PositiveInteger defaultVal) {
		super(s, PositiveInteger.class, defaultVal);
	}

	@Override
	public Setting<PositiveInteger> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(PositiveInteger.newInstance(value));
	}
	
}
