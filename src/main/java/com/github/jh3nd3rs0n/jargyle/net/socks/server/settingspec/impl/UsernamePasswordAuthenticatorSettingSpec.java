package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.userpassauth.UsernamePasswordAuthenticator;

final class UsernamePasswordAuthenticatorSettingSpec 
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
