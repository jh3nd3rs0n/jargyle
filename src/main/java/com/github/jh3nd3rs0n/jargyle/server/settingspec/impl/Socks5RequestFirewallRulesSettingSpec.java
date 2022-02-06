package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5RequestFirewallRules;

public final class Socks5RequestFirewallRulesSettingSpec 
	extends SettingSpec<Socks5RequestFirewallRules> {
	
	public Socks5RequestFirewallRulesSettingSpec(
			final String s,	final Socks5RequestFirewallRules defaultVal) {
		super(s, Socks5RequestFirewallRules.class, defaultVal);
	}
	
	@Override
	public Setting<Socks5RequestFirewallRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5RequestFirewallRules.newInstance(value));
	}
	
}