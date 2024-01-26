package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class Socks5MethodsSettingSpec extends SettingSpec<Methods> {

	public Socks5MethodsSettingSpec(final String n, final Methods defaultVal) {
		super(n, Methods.class, defaultVal);
	}

	@Override
	protected Methods parse(final String value) {
		return Methods.newInstanceFrom(value);
	}

}
