package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5ReplyFirewallRules;

public final class Socks5ReplyFirewallRulesSettingSpec 
	extends SettingSpec<Socks5ReplyFirewallRules> {

	public Socks5ReplyFirewallRulesSettingSpec(
			final String s, final Socks5ReplyFirewallRules defaultVal) {
		super(s, Socks5ReplyFirewallRules.class, defaultVal);
	}
	
	@Override
	public Setting<Socks5ReplyFirewallRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5ReplyFirewallRules.newInstance(value));
	}

}
