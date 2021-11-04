package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5ReplyRules;

public final class Socks5ReplyRulesSettingSpec 
	extends SettingSpec<Socks5ReplyRules> {

	public Socks5ReplyRulesSettingSpec(
			final Object permission,
			final String s,
			final Socks5ReplyRules defaultVal) {
		super(permission, s, Socks5ReplyRules.class, defaultVal);
	}
	
	@Override
	public Setting<Socks5ReplyRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5ReplyRules.newInstance(value));
	}

}
