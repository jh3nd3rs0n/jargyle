package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;

public final class Socks5UserpassauthUsernameSettingSpec extends SettingSpec<String> {

	private static String getValidatedUsername(final String s) {
		UsernamePasswordRequest.validateUsername(s);
		return s;
	}

	public Socks5UserpassauthUsernameSettingSpec(final String s, final String defaultVal) {
		super(s, String.class, getValidatedUsername(defaultVal));
	}

	@Override
	public Setting<String> newSetting(final String value) {
		return super.newSetting(getValidatedUsername(value));
	}
	
	@Override
	public Setting<String> newSettingOfParsableValue(final String value) {
		return super.newSetting(getValidatedUsername(value));
	}

}
