package jargyle.internal.net.socks.server.settingspec.impl;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

final class StringSettingSpec extends SettingSpec<String> {

	public StringSettingSpec(final String s, final String defaultVal) {
		super(s, String.class, defaultVal);
	}

	@Override
	public Setting<String> newSettingOfParsableValue(final String value) {
		return super.newSetting(value);
	}
	
}
