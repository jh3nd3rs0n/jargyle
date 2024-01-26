package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class PortRangesSettingSpec extends SettingSpec<PortRanges> {

	public PortRangesSettingSpec(final String n, final PortRanges defaultVal) {
		super(n, PortRanges.class, defaultVal);
	}

	@Override
	protected PortRanges parse(final String value) {
		return PortRanges.newInstanceFrom(value);
	}

}
