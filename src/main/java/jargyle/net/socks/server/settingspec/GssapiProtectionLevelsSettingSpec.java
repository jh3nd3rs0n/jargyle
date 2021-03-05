package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevels;

public final class GssapiProtectionLevelsSettingSpec extends SettingSpec {

	public GssapiProtectionLevelsSettingSpec(
			final String s, final GssapiProtectionLevels defaultVal) {
		super(s, defaultVal);
	}
	
	@Override
	public Setting getDefaultSetting() {
		return Setting.newInstance(
				this, GssapiProtectionLevels.class.cast(this.defaultValue));
	}

	@Override
	public Setting newSetting(final Object value) {
		GssapiProtectionLevels val = GssapiProtectionLevels.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		return Setting.newInstance(
				this, GssapiProtectionLevels.newInstance(value));
	}

}
