package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Request;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

import java.util.Arrays;

public final class Socks5UserpassMethodEncryptedPasswordSettingSpec 
	extends SettingSpec<EncryptedPassword> {

	private static EncryptedPassword getValidatedEncryptedPassword(
			final EncryptedPassword encryptedPassword) {
		char[] password = encryptedPassword.getPassword();
		Request.validatePassword(password);
		Arrays.fill(password, '\0');
		return encryptedPassword;
	}

	public Socks5UserpassMethodEncryptedPasswordSettingSpec(
			final String n, final EncryptedPassword defaultVal) {
		super(
				n, 
				EncryptedPassword.class,
				(defaultVal == null) ?
						null : getValidatedEncryptedPassword(defaultVal));
	}

	@Override
	protected EncryptedPassword parse(final String value) {
		return EncryptedPassword.newInstance(value.toCharArray());
	}

	@Override
	protected EncryptedPassword validate(final EncryptedPassword value) {
		return getValidatedEncryptedPassword(value);
	}

}
