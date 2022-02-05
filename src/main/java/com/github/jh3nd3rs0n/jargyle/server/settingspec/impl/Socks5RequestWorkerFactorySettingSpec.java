package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestWorkerFactory;

public final class Socks5RequestWorkerFactorySettingSpec 
	extends SettingSpec<Socks5RequestWorkerFactory> {

	public Socks5RequestWorkerFactorySettingSpec(
			final String s, final Socks5RequestWorkerFactory defaultVal) {
		super(s, Socks5RequestWorkerFactory.class, defaultVal);
	}

	@Override
	public Setting<Socks5RequestWorkerFactory> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(Socks5RequestWorkerFactory.newInstance(value));
	}
	
}
