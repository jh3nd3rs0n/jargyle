package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;

final class EncryptedPasswordSettingSpec
	extends SettingSpec<EncryptedPassword> {

	public EncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal) {
		super(s, EncryptedPassword.class, defaultVal);
	}

	@Override
	public Setting<EncryptedPassword> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(EncryptedPassword.newInstance(
				value.toCharArray()));
	}

}
