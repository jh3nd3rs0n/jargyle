package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.client.v5.UsernamePassword;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class UsernamePasswordSettingSpec extends SettingSpec {

	public UsernamePasswordSettingSpec(
			final String s, final UsernamePassword defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		UsernamePassword val = UsernamePassword.class.cast(value);
		return super.newSetting(val);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(UsernamePassword.newInstance(value));
	}
	
}
