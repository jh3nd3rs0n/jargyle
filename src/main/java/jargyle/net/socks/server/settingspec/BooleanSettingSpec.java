package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class BooleanSettingSpec extends SettingSpec {

	public BooleanSettingSpec(final String s, final Boolean defaultVal) {
		super(s, Boolean.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(Boolean.valueOf(value));
	}	

}
