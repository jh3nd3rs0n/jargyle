package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public final class Socks5MethodsSettingSpec extends SettingSpec<Methods> {

	public Socks5MethodsSettingSpec(final String s, final Methods defaultVal) {
		super(s, Methods.class, defaultVal);
	}

	@Override
	public Setting<Methods> newSettingOfParsableValue(final String value) {
		return super.newSetting(Methods.newInstance(value));
	}
	
}
