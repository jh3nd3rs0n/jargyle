package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;

public final class UsernamePasswordAuthenticatorSettingSpec 
	extends SettingSpec<UsernamePasswordAuthenticator> {

	public UsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		super(s, UsernamePasswordAuthenticator.class, defaultVal);
	}

	@Override
	public Setting<UsernamePasswordAuthenticator> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(UsernamePasswordAuthenticator.newInstance(
				value));
	}

}
