package jargyle.internal.net.socks.server.settingspec.impl;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

final class BooleanSettingSpec extends SettingSpec<Boolean> {

	public BooleanSettingSpec(final String s, final Boolean defaultVal) {
		super(s, Boolean.class, defaultVal);
	}

	@Override
	public Setting<Boolean> newSettingOfParsableValue(final String value) {
		return super.newSetting(Boolean.valueOf(value));
	}	

}
