package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.Socks5RequestCriteria;

public final class Socks5RequestCriteriaSettingSpec extends SettingSpec {

	public Socks5RequestCriteriaSettingSpec(
			final String s, final Socks5RequestCriteria defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		Socks5RequestCriteria val = Socks5RequestCriteria.class.cast(value);
		return super.newSetting(val);
	}

	@Override
	public Setting newSetting(final String value) {
		throw new UnsupportedOperationException(String.format(
				"%s does not accept a String representation of %s",
				this,
				Socks5RequestCriteria.class.getName()));
	}

}
