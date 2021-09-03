package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;

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
