package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class PortSettingSpec extends SettingSpec<Port> {

	public PortSettingSpec(final String s, final Port defaultVal) {
		super(s, Port.class, defaultVal);
	}

	@Override
	public Setting<Port> newSettingOfParsableValue(final String value) {
		return super.newSetting(Port.newInstance(value));
	}
	
}
