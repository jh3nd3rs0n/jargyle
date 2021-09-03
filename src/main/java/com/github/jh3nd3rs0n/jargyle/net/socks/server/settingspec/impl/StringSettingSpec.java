package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;

public final class StringSettingSpec extends SettingSpec<String> {

	public StringSettingSpec(
			final Object permission, 
			final String s, 
			final String defaultVal) {
		super(permission, s, String.class, defaultVal);
	}

	@Override
	public Setting<String> newSettingOfParsableValue(final String value) {
		return super.newSetting(value);
	}
	
}
