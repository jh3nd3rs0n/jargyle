package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientRoutingRules;

public final class ClientRoutingRulesSettingSpec 
	extends SettingSpec<ClientRoutingRules> {

	public ClientRoutingRulesSettingSpec(
			final Object permission, 
			final String s,
			final ClientRoutingRules defaultVal) {
		super(permission, s, ClientRoutingRules.class, defaultVal);
	}

	@Override
	public Setting<ClientRoutingRules> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(ClientRoutingRules.newInstance(value));
	}

}
