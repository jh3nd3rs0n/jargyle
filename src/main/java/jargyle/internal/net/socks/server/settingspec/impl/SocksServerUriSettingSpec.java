package jargyle.internal.net.socks.server.settingspec.impl;

import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

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
