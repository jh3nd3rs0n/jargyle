package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;

final class SocksServerUriSettingSpec 
	extends SettingSpec<SocksServerUri> {

	public SocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		super(s, SocksServerUri.class, defaultVal);
	}

	@Override
	public Setting<SocksServerUri> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(SocksServerUri.newInstance(value));
	}

}