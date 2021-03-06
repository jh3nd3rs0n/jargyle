package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.util.Strings;

public final class StringsSettingSpec extends SettingSpec {

	public StringsSettingSpec(final String s, final Strings defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		Strings val = Strings.class.cast(value);
		return super.newSetting(val);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(Strings.newInstance(value));
	}

}
