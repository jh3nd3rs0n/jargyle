package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;

final class MethodsSettingSpec extends SettingSpec<Methods> {

	public MethodsSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Methods defaultVal) {
		super(permissionObj, s, Methods.class, defaultVal);
	}

	@Override
	public Setting<Methods> newSettingOfParsableValue(final String value) {
		return super.newSetting(Methods.newInstance(value));
	}
	
}
