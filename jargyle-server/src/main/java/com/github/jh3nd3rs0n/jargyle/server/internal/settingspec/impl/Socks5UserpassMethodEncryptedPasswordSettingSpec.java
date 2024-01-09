package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.UsernamePasswordRequest;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class Socks5UserpassMethodEncryptedPasswordSettingSpec 
	extends SettingSpec<EncryptedPassword> {

	private static EncryptedPassword getValidatedEncryptedPassword(
			final EncryptedPassword encryptedPassword) {
		char[] password = encryptedPassword.getPassword();
		UsernamePasswordRequest.validatePassword(password);
		Arrays.fill(password, '\0');
		return encryptedPassword;
	}

	public Socks5UserpassMethodEncryptedPasswordSettingSpec(
			final String n, final EncryptedPassword defaultVal) {
		super(
				n, 
				EncryptedPassword.class, 
				getValidatedEncryptedPassword(defaultVal));
	}

	@Override
	public Setting<EncryptedPassword> newSetting(
			final EncryptedPassword value) {
		return super.newSetting(getValidatedEncryptedPassword(value));
	}
	
	@Override
	public Setting<EncryptedPassword> newSettingWithParsedValue(
			final String value) {
		return super.newSetting(getValidatedEncryptedPassword(
				EncryptedPassword.newInstance(value.toCharArray())));
	}

}
