package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRules;

public final class Socks5RequestRulesSettingSpec 
	extends SettingSpec<Socks5RequestRules> {
	
	public Socks5RequestRulesSettingSpec(
			final Object permission,
			final String s,
			final Socks5RequestRules defaultVal) {
		super(permission, s, Socks5RequestRules.class, defaultVal);
	}
	
	@Override
	public Setting<Socks5RequestRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5RequestRules.newInstance(value));
	}
	
}
