package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.security.EncryptedPassword;

public final class EncryptedPasswordSettingSpec extends SettingSpec {

	public EncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal) {
		super(s, defaultVal);
	}
	
	@Override
	public Setting getDefaultSetting() {
		return Setting.newInstance(
				this, EncryptedPassword.class.cast(this.defaultValue));
	}

	@Override
	public Setting newSetting(final Object value) {
		EncryptedPassword val = EncryptedPassword.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		return Setting.newInstance(
				this, EncryptedPassword.newInstance(value.toCharArray()));
	}

}
