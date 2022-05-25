package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;

public final class UserEncryptedPasswordPropertySpec
	extends PropertySpec<EncryptedPassword> {

	private static EncryptedPassword getValidatedEncryptedPassword(
			final EncryptedPassword encryptedPassword) {
		char[] password = encryptedPassword.getPassword();
		UsernamePasswordRequest.validatePassword(password);
		Arrays.fill(password, '\0');
		return encryptedPassword;
	}
	
	public UserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		super(
				s, 
				EncryptedPassword.class, 
				getValidatedEncryptedPassword(defaultVal));
	}

	@Override
	public Property<EncryptedPassword> newProperty(
			final EncryptedPassword value) {
		return super.newProperty(getValidatedEncryptedPassword(value));
	}

	@Override
	public Property<EncryptedPassword> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(getValidatedEncryptedPassword(
				EncryptedPassword.newInstance(value.toCharArray())));
	}

}
