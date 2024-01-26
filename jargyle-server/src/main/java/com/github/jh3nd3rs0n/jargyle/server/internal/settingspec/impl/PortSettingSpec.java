package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class PortSettingSpec extends SettingSpec<Port> {

	public PortSettingSpec(final String n, final Port defaultVal) {
		super(n, Port.class, defaultVal);
	}

	@Override
	protected Port parse(final String value) {
		return Port.valueOf(value);
	}

}
