package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.userpassauth.UsernamePasswordAuthenticator;

public final class UsernamePasswordAuthenticatorSettingSpec 
	extends SettingSpec<UsernamePasswordAuthenticator> {

	public UsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		super(s, UsernamePasswordAuthenticator.class, defaultVal);
	}

	@Override
	public Setting<UsernamePasswordAuthenticator> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(UsernamePasswordAuthenticator.newInstance(
				value));
	}

}
