package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5UdpFirewallRules;

public final class Socks5UdpFirewallRulesSettingSpec
		extends SettingSpec<Socks5UdpFirewallRules> {

	public Socks5UdpFirewallRulesSettingSpec(
			final Object permission, 
			final String s, 
			final Socks5UdpFirewallRules defaultVal) {
		super(permission, s, Socks5UdpFirewallRules.class, defaultVal);
	}

	@Override
	public Setting<Socks5UdpFirewallRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5UdpFirewallRules.newInstance(value));
	}
	
}
