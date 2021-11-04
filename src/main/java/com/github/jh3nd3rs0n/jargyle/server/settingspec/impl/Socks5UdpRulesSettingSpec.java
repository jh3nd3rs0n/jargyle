package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5UdpRules;

public final class Socks5UdpRulesSettingSpec
		extends SettingSpec<Socks5UdpRules> {

	public Socks5UdpRulesSettingSpec(
			final Object permission, 
			final String s, 
			final Socks5UdpRules defaultVal) {
		super(permission, s, Socks5UdpRules.class, defaultVal);
	}

	@Override
	public Setting<Socks5UdpRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5UdpRules.newInstance(value));
	}
	
}
