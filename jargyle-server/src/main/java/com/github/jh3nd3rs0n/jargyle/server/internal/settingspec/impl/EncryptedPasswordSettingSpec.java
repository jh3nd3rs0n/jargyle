package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class EncryptedPasswordSettingSpec
	extends SettingSpec<EncryptedPassword> {

	public EncryptedPasswordSettingSpec(
			final String n, final EncryptedPassword defaultVal) {
		super(n, EncryptedPassword.class, defaultVal);
	}

	@Override
	protected EncryptedPassword parse(final String value) {
		return EncryptedPassword.newInstance(value.toCharArray());
	}

}
