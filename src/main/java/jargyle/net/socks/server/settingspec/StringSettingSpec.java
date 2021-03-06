package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class StringSettingSpec extends SettingSpec {

	public StringSettingSpec(final String s, final String defaultVal) {
		super(s, String.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting((Object) value);
	}
	
}
