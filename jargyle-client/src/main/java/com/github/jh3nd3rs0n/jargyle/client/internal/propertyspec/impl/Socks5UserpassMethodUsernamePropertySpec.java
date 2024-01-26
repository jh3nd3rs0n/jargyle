package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.UsernamePasswordRequest;

public final class Socks5UserpassMethodUsernamePropertySpec extends PropertySpec<String> {

	private static String getValidatedUsername(final String username) {
		UsernamePasswordRequest.validateUsername(username);
		return username;
	}
	
	public Socks5UserpassMethodUsernamePropertySpec(
			final String n, final String defaultVal) {
		super(n, String.class, getValidatedUsername(defaultVal));
	}

	@Override
	protected String parse(final String value) {
		return value;
	}

	@Override
	protected String validate(final String value) {
		return getValidatedUsername(value);
	}

}
