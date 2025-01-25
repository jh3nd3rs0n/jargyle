package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Request;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class Socks5UserpassMethodUsernameSettingSpec extends SettingSpec<String> {

	private static String getValidatedUsername(final String username) {
		Request.validateUsername(username);
		return username;
	}

	public Socks5UserpassMethodUsernameSettingSpec(
			final String n, final String defaultVal) {
		super(
				n,
				String.class,
				(defaultVal == null) ?
						null : getValidatedUsername(defaultVal));
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
