package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.ClientFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class ClientFirewallRulesSettingSpec 
	extends SettingSpec<ClientFirewallRules> {

	public ClientFirewallRulesSettingSpec(
			final Object permission, 
			final String s,
			final ClientFirewallRules defaultVal) {
		super(permission, s, ClientFirewallRules.class, defaultVal);
	}

	@Override
	public Setting<ClientFirewallRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(ClientFirewallRules.newInstance(value));
	}
	
}
