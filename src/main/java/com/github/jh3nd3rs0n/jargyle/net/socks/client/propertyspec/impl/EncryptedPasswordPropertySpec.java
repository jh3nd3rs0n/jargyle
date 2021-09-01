package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;

final class EncryptedPasswordPropertySpec 
	extends PropertySpec<EncryptedPassword> {

	public EncryptedPasswordPropertySpec(
			final Object permissionObj, 
			final String s, 
			final EncryptedPassword defaultVal) {
		super(permissionObj, s, EncryptedPassword.class, defaultVal);
	}

	@Override
	public Property<EncryptedPassword> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(EncryptedPassword.newInstance(
				value.toCharArray()));
	}

}
