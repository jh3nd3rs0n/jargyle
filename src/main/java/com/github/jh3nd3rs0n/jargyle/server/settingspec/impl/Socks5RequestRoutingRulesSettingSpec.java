package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRoutingRules;

public final class Socks5RequestRoutingRulesSettingSpec 
	extends SettingSpec<Socks5RequestRoutingRules> {

	public Socks5RequestRoutingRulesSettingSpec(
			final Object permission, 
			final String s,
			final Socks5RequestRoutingRules defaultVal) {
		super(permission, s, Socks5RequestRoutingRules.class, defaultVal);
	}

	@Override
	public Setting<Socks5RequestRoutingRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5RequestRoutingRules.newInstance(value));
	}

}
