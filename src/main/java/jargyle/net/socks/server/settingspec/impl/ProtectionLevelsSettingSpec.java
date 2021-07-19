package jargyle.net.socks.server.settingspec.impl;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;

final class ProtectionLevelsSettingSpec 
	extends SettingSpec<ProtectionLevels> {

	public ProtectionLevelsSettingSpec(
			final String s, final ProtectionLevels defaultVal) {
		super(s, ProtectionLevels.class, defaultVal);
	}

	@Override
	public Setting<ProtectionLevels> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(ProtectionLevels.newInstance(value));
	}

}
