package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.util.NonnegativeInteger;

final class NonnegativeIntegerSettingSpec 
	extends SettingSpec<NonnegativeInteger> {

	public NonnegativeIntegerSettingSpec(
			final String s, final NonnegativeInteger defaultVal) {
		super(s, NonnegativeInteger.class, defaultVal);
	}

	@Override
	public Setting<NonnegativeInteger> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(NonnegativeInteger.newInstance(value));
	}

}
