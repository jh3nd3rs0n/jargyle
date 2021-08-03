package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;

final class PortSettingSpec extends SettingSpec<Port> {

	public PortSettingSpec(final String s, final Port defaultVal) {
		super(s, Port.class, defaultVal);
	}

	@Override
	public Setting<Port> newSettingOfParsableValue(final String value) {
		return super.newSetting(Port.newInstance(value));
	}
	
}
