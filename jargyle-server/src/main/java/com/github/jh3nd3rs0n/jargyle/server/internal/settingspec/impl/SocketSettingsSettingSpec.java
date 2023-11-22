package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class SocketSettingsSettingSpec 
	extends SettingSpec<SocketSettings> {

	public SocketSettingsSettingSpec(
			final String n, final SocketSettings defaultVal) {
		super(n, SocketSettings.class, defaultVal);
	}

	@Override
	public Setting<SocketSettings> newSettingWithParsableValue(
			final String value) {
		return super.newSetting(SocketSettings.newInstance(value));
	}
	
}
