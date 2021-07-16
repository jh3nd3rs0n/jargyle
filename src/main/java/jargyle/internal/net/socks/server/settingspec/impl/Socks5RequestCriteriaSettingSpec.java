package jargyle.internal.net.socks.server.settingspec.impl;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.Socks5RequestCriteria;

final class Socks5RequestCriteriaSettingSpec 
	extends SettingSpec<Socks5RequestCriteria> {

	public Socks5RequestCriteriaSettingSpec(
			final String s, final Socks5RequestCriteria defaultVal) {
		super(s, Socks5RequestCriteria.class, defaultVal);
	}

	@Override
	public Setting<Socks5RequestCriteria> newSettingOfParsableValue(
			final String value) {
		throw new UnsupportedOperationException(String.format(
				"%s does not accept a String representation of %s",
				this,
				Socks5RequestCriteria.class.getName()));
	}

}
