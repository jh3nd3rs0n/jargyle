package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevels;

public final class GssapiProtectionLevelsSettingSpec 
	extends SettingSpec<GssapiProtectionLevels> {

	public GssapiProtectionLevelsSettingSpec(
			final String s, final GssapiProtectionLevels defaultVal) {
		super(s, GssapiProtectionLevels.class, defaultVal);
	}

	@Override
	public Setting<GssapiProtectionLevels> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(GssapiProtectionLevels.newInstance(value));
	}

}
