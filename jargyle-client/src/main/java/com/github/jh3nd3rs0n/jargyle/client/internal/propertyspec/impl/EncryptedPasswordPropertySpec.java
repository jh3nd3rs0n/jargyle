package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;

public final class EncryptedPasswordPropertySpec 
	extends PropertySpec<EncryptedPassword> {

	public EncryptedPasswordPropertySpec(
			final String n, final EncryptedPassword defaultVal) {
		super(n, EncryptedPassword.class, defaultVal);
	}

	@Override
	protected EncryptedPassword parse(final String value) {
		return EncryptedPassword.newInstance(value.toCharArray());
	}

}
