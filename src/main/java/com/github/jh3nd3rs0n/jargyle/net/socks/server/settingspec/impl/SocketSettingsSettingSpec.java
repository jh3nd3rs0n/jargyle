package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;

final class SocketSettingsSettingSpec extends SettingSpec<SocketSettings> {

	public SocketSettingsSettingSpec(
			final Object permissionObj, 
			final String s, 
			final SocketSettings defaultVal) {
		super(permissionObj, s, SocketSettings.class, defaultVal);
	}

	@Override
	public Setting<SocketSettings> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(SocketSettings.newInstance(value));
	}
	
}
