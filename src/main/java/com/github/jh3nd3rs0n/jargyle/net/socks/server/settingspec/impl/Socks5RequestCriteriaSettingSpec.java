package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.Socks5RequestCriteria;

public final class Socks5RequestCriteriaSettingSpec 
	extends SettingSpec<Socks5RequestCriteria> {

	public Socks5RequestCriteriaSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Socks5RequestCriteria defaultVal) {
		super(permissionObj, s, Socks5RequestCriteria.class, defaultVal);
	}

	@Override
	public Setting<Socks5RequestCriteria> newSettingOfParsableValue(
			final String value) {
		throw new UnsupportedOperationException(String.format(
				"%s does not accept a String representation of %s",
				this,
				Socks5RequestCriteria.class.getName()));
	}

}
