package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class Socks5GssapiauthProtectionLevelsSettingSpec 
	extends SettingSpec<ProtectionLevels> {

	public Socks5GssapiauthProtectionLevelsSettingSpec(
			final String n, final ProtectionLevels defaultVal) {
		super(n, ProtectionLevels.class, defaultVal);
	}

	@Override
	public Setting<ProtectionLevels> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(ProtectionLevels.newInstance(value));
	}

}
