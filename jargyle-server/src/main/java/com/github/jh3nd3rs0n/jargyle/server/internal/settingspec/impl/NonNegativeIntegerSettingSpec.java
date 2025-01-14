package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class NonNegativeIntegerSettingSpec
	extends SettingSpec<NonNegativeInteger> {

	public NonNegativeIntegerSettingSpec(
			final String n, final NonNegativeInteger defaultVal) {
		super(n, NonNegativeInteger.class, defaultVal);
	}

	@Override
	protected NonNegativeInteger parse(final String value) {
		return NonNegativeInteger.valueOf(value);
	}

}
