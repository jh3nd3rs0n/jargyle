package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;

public final class EncryptedPasswordPropertySpec 
	extends PropertySpec<EncryptedPassword> {

	public EncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		super(s, EncryptedPassword.class, defaultVal);
	}

	@Override
	public Property<EncryptedPassword> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(EncryptedPassword.newInstance(
				value.toCharArray()));
	}

}
