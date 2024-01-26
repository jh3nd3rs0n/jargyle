package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class RuleSettingSpec extends SettingSpec<Rule> {

	public RuleSettingSpec(final String n, final Rule defaultVal) {
		super(n, Rule.class, defaultVal);
	}

	@Override
	protected Rule parse(final String value) {
		return Rule.newInstanceFrom(value);
	}

}
