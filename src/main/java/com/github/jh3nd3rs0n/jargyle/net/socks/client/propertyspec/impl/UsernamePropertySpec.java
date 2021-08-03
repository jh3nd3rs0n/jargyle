package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.v5.userpassauth.UsernamePassword;

final class UsernamePropertySpec extends PropertySpec<String> {

	private static String getValidatedUsername(final String s) {
		UsernamePassword.validateUsername(s);
		return s;
	}
	
	public UsernamePropertySpec(final String s, final String defaultVal) {
		super(s, String.class, getValidatedUsername(defaultVal));
	}

	@Override
	public Property<String> newProperty(final String value) {
		return super.newProperty(getValidatedUsername(value));
	}

	@Override
	public Property<String> newPropertyOfParsableValue(final String value) {
		return super.newProperty(getValidatedUsername(value));
	}

}
