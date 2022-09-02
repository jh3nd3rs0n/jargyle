package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class PortRangesSettingSpec extends SettingSpec<PortRanges> {

	public PortRangesSettingSpec(final String s, final PortRanges defaultVal) {
		super(s, PortRanges.class, defaultVal);
	}

	@Override
	public Setting<PortRanges> newSettingOfParsableValue(final String value) {
		return super.newSetting(PortRanges.newInstance(value));
	}

}
