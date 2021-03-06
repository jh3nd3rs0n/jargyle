package jargyle.net.socks.server.settingspec;

import jargyle.net.Port;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class PortSettingSpec extends SettingSpec {

	public PortSettingSpec(final String s, final Port defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		Port val = Port.class.cast(value);
		return super.newSetting(val);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(Port.newInstance(value));
	}
	
}
