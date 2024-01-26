package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.UsernamePasswordRequest;

import java.util.Arrays;

public final class Socks5UserpassMethodEncryptedPasswordPropertySpec
	extends PropertySpec<EncryptedPassword> {

	private static EncryptedPassword getValidatedEncryptedPassword(
			final EncryptedPassword encryptedPassword) {
		char[] password = encryptedPassword.getPassword();
		UsernamePasswordRequest.validatePassword(password);
		Arrays.fill(password, '\0');
		return encryptedPassword;
	}
	
	public Socks5UserpassMethodEncryptedPasswordPropertySpec(
			final String n, final EncryptedPassword defaultVal) {
		super(
				n, 
				EncryptedPassword.class, 
				getValidatedEncryptedPassword(defaultVal));
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
