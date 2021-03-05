package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.util.PositiveInteger;

public final class PositiveIntegerSettingSpec extends SettingSpec {

	public PositiveIntegerSettingSpec(
			final String s, final PositiveInteger defaultVal) {
		super(s, defaultVal);
	}
	
	@Override
	public Setting getDefaultSetting() {
		return Setting.newInstance(
				this, PositiveInteger.class.cast(this.defaultValue));
	}

	@Override
	public Setting newSetting(final Object value) {
		PositiveInteger val = PositiveInteger.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		return Setting.newInstance(this, PositiveInteger.newInstance(value));
	}
	
}
