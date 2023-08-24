package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class EncryptedPasswordSettingSpec
	extends SettingSpec<EncryptedPassword> {

	public EncryptedPasswordSettingSpec(
			final String n, final EncryptedPassword defaultVal) {
		super(n, EncryptedPassword.class, defaultVal);
	}

	@Override
	public Setting<EncryptedPassword> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(EncryptedPassword.newInstance(
				value.toCharArray()));
	}

}
