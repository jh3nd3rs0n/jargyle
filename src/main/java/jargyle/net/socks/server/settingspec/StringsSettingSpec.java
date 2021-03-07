package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.util.Strings;

public final class StringsSettingSpec extends SettingSpec<Strings> {

	public StringsSettingSpec(final String s, final Strings defaultVal) {
		super(s, Strings.class, defaultVal);
	}

	@Override
	public Setting<Strings> newSettingOfParsableValue(final String value) {
		return super.newSetting(Strings.newInstance(value));
	}

}
