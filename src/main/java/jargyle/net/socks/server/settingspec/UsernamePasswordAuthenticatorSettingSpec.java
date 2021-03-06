package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.UsernamePasswordAuthenticator;

public final class UsernamePasswordAuthenticatorSettingSpec extends SettingSpec {

	public UsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		UsernamePasswordAuthenticator val = 
				UsernamePasswordAuthenticator.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		return Setting.newInstance(
				this, UsernamePasswordAuthenticator.newInstance(value));
	}

}
