package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.ClientRules;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class ClientRulesSettingSpec extends SettingSpec<ClientRules> {

	public ClientRulesSettingSpec(
			final Object permission, 
			final String s,
			final ClientRules defaultVal) {
		super(permission, s, ClientRules.class, defaultVal);
	}

	@Override
	public Setting<ClientRules> newSettingOfParsableValue(final String value) {
		return super.newSetting(ClientRules.newInstance(value));
	}
	
}
