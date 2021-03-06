package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.UsernamePasswordAuthenticator;

public final class UsernamePasswordAuthenticatorSettingSpec extends SettingSpec {

	public UsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		super(s, UsernamePasswordAuthenticator.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(UsernamePasswordAuthenticator.newInstance(
				value));
	}

}
