package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class BooleanSettingSpec extends SettingSpec {

	public BooleanSettingSpec(final String s, final Boolean defaultVal) {
		super(s, defaultVal);
	}
	
	@Override
	public Setting getDefaultSetting() {
		return Setting.newInstance(this, Boolean.class.cast(this.defaultValue));
	}

	@Override
	public Setting newSetting(final Object value) {
		Boolean val = Boolean.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		return Setting.newInstance(this, Boolean.valueOf(value));
	}	

}
