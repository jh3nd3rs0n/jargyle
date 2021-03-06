package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.transport.v5.AuthMethods;

public final class AuthMethodsSettingSpec extends SettingSpec {

	public AuthMethodsSettingSpec(
			final String s, final AuthMethods defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		AuthMethods val = AuthMethods.class.cast(value);
		return super.newSetting(val);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(AuthMethods.newInstance(value));
	}
	
}
