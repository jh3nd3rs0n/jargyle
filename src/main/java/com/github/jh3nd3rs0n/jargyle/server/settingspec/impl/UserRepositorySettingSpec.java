package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;

public final class UserRepositorySettingSpec 
	extends SettingSpec<UserRepository> {

	public UserRepositorySettingSpec(
			final String s, final UserRepository defaultVal) {
		super(s, UserRepository.class, defaultVal);
	}

	@Override
	public Setting<UserRepository> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(UserRepository.newInstance(value));
	}

}
