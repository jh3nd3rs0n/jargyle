package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class SocketSettingsSettingSpec 
	extends SettingSpec<SocketSettings> {

	public SocketSettingsSettingSpec(
			final Object permission, 
			final String s, 
			final SocketSettings defaultVal) {
		super(permission, s, SocketSettings.class, defaultVal);
	}

	@Override
	public Setting<SocketSettings> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(SocketSettings.newInstance(value));
	}
	
}
