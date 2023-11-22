package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.UsernamePasswordRequest;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class Socks5UserpassMethodUsernameSettingSpec extends SettingSpec<String> {

	private static String getValidatedUsername(final String username) {
		UsernamePasswordRequest.validateUsername(username);
		return username;
	}

	public Socks5UserpassMethodUsernameSettingSpec(
			final String n, final String defaultVal) {
		super(n, String.class, getValidatedUsername(defaultVal));
	}

	@Override
	public Setting<String> newSetting(final String value) {
		return super.newSetting(getValidatedUsername(value));
	}
	
	@Override
	public Setting<String> newSettingWithParsableValue(final String value) {
		return super.newSetting(getValidatedUsername(value));
	}

}
