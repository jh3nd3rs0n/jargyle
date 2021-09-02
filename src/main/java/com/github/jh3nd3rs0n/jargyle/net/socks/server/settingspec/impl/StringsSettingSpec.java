package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

public final class StringsSettingSpec extends SettingSpec<Strings> {

	public StringsSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Strings defaultVal) {
		super(permissionObj, s, Strings.class, defaultVal);
	}

	@Override
	public Setting<Strings> newSettingOfParsableValue(final String value) {
		return super.newSetting(Strings.newInstance(value));
	}

}
