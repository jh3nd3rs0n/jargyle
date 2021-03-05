package jargyle.net.socks.server.settingspec;

import jargyle.net.SocketSettings;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class SocketSettingsSettingSpec extends SettingSpec {

	public SocketSettingsSettingSpec(
			final String s, final SocketSettings defaultVal) {
		super(s, defaultVal);
	}
	
	@Override
	public Setting getDefaultSetting() {
		return Setting.newInstance(
				this, SocketSettings.class.cast(this.defaultValue));
	}

	@Override
	public Setting newSetting(final Object value) {
		SocketSettings val = SocketSettings.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		return Setting.newInstance(this, SocketSettings.newInstance(value));
	}
	
}
