package jargyle.net.socks.server.settingspec;

import jargyle.net.SocketSettings;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class SocketSettingsSettingSpec 
	extends SettingSpec<SocketSettings> {

	public SocketSettingsSettingSpec(
			final String s, final SocketSettings defaultVal) {
		super(s, SocketSettings.class, defaultVal);
	}

	@Override
	public Setting<SocketSettings> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(SocketSettings.newInstance(value));
	}
	
}
