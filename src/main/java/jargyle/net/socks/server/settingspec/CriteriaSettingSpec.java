package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.util.Criteria;

public final class CriteriaSettingSpec extends SettingSpec {

	public CriteriaSettingSpec(final String s, final Criteria defaultVal) {
		super(s, Criteria.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(Criteria.newInstance(value));
	}

}
