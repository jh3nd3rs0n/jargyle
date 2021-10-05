package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.text.Criteria;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class CriteriaSettingSpec extends SettingSpec<Criteria> {

	public CriteriaSettingSpec(
			final Object permission, 
			final String s, 
			final Criteria defaultVal) {
		super(permission, s, Criteria.class, defaultVal);
	}

	@Override
	public Setting<Criteria> newSettingOfParsableValue(final String value) {
		return super.newSetting(Criteria.newInstance(value));
	}

}
