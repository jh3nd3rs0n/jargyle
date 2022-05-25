package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;

public final class UsernamePropertySpec extends PropertySpec<String> {

	private static String getValidatedUsername(final String s) {
		UsernamePasswordRequest.validateUsername(s);
		return s;
	}
	
	public UsernamePropertySpec(final String s,	final String defaultVal) {
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
