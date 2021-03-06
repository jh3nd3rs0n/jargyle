package jargyle.net.socks.server.settingspec;

import jargyle.net.Port;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class PortSettingSpec extends SettingSpec {

	public PortSettingSpec(final String s, final Port defaultVal) {
		super(s, Port.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(Port.newInstance(value));
	}
	
}
