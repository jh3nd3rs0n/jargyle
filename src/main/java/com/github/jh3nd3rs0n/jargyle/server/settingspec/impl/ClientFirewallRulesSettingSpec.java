package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientFirewallRules;

public final class ClientFirewallRulesSettingSpec 
	extends SettingSpec<ClientFirewallRules> {

	public ClientFirewallRulesSettingSpec(
			final String s, final ClientFirewallRules defaultVal) {
		super(s, ClientFirewallRules.class, defaultVal);
	}

	@Override
	public Setting<ClientFirewallRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(ClientFirewallRules.newInstance(value));
	}
	
}
