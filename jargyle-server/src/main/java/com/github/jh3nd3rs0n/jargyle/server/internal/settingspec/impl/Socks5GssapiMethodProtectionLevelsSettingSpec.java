package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class Socks5GssapiMethodProtectionLevelsSettingSpec 
	extends SettingSpec<ProtectionLevels> {

	public Socks5GssapiMethodProtectionLevelsSettingSpec(
			final String n, final ProtectionLevels defaultVal) {
		super(n, ProtectionLevels.class, defaultVal);
	}

	@Override
	protected ProtectionLevels parse(final String value) {
		return ProtectionLevels.newInstanceFrom(value);
	}

}
