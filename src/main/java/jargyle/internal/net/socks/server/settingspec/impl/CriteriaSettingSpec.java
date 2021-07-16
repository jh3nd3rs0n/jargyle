package jargyle.internal.net.socks.server.settingspec.impl;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.util.Criteria;

final class CriteriaSettingSpec extends SettingSpec<Criteria> {

	public CriteriaSettingSpec(final String s, final Criteria defaultVal) {
		super(s, Criteria.class, defaultVal);
	}

	@Override
	public Setting<Criteria> newSettingOfParsableValue(final String value) {
		return super.newSetting(Criteria.newInstance(value));
	}

}
