package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.socks5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class UsernamePasswordSettingSpec 
	extends SettingSpec<UsernamePassword> {

	public UsernamePasswordSettingSpec(
			final Object permission, 
			final String s, 
			final UsernamePassword defaultVal) {
		super(permission, s, UsernamePassword.class, defaultVal);
	}

	@Override
	public Setting<UsernamePassword> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(UsernamePassword.newInstance(value));
	}
	
}