package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.Socks5RequestWorkerFactory;

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
