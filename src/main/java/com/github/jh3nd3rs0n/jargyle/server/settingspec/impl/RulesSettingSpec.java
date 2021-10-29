package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Rules;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class RulesSettingSpec extends SettingSpec<Rules> {

	public RulesSettingSpec(
			final Object permission, 
			final String s,
			final Rules defaultVal) {
		super(permission, s, Rules.class, defaultVal);
	}

	@Override
	public Setting<Rules> newSettingOfParsableValue(final String value) {
		throw new UnsupportedOperationException(String.format(
				"%s does not accept a String representation of %s",
				this,
				Rules.class.getName()));
	}
	
}
