package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;

public final class Socks5UserpassauthUserRepositorySettingSpec 
	extends SettingSpec<UserRepository> {

	public Socks5UserpassauthUserRepositorySettingSpec(
			final String n, final UserRepository defaultVal) {
		super(n, UserRepository.class, defaultVal);
	}

	@Override
	public Setting<UserRepository> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(UserRepository.newInstance(value));
	}

}
