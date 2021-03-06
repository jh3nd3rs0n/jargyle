package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.security.EncryptedPassword;

public final class EncryptedPasswordSettingSpec extends SettingSpec {

	public EncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal) {
		super(s, EncryptedPassword.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(EncryptedPassword.newInstance(
				value.toCharArray()));
	}

}
