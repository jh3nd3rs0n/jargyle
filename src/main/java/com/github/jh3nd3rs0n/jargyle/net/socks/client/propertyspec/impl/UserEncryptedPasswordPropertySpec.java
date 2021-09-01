package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;

final class UserEncryptedPasswordPropertySpec
	extends PropertySpec<EncryptedPassword> {

	private static EncryptedPassword getValidatedEncryptedPassword(
			final EncryptedPassword encryptedPassword) {
		char[] password = encryptedPassword.getPassword();
		UsernamePassword.validatePassword(password);
		Arrays.fill(password, '\0');
		return encryptedPassword;
	}
	
	public UserEncryptedPasswordPropertySpec(
			final Object permissionObj, 
			final String s, 
			final EncryptedPassword defaultVal) {
		super(
				permissionObj, 
				s, 
				EncryptedPassword.class, getValidatedEncryptedPassword(defaultVal));
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
