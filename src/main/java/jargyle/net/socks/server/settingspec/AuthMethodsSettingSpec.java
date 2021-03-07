package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.transport.v5.AuthMethods;

public final class AuthMethodsSettingSpec extends SettingSpec<AuthMethods> {

	public AuthMethodsSettingSpec(
			final String s, final AuthMethods defaultVal) {
		super(s, AuthMethods.class, defaultVal);
	}

	@Override
	public Setting<AuthMethods> newSettingOfParsableValue(final String value) {
		return super.newSetting(AuthMethods.newInstance(value));
	}
	
}
