package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class SocksServerUriSettingSpec extends SettingSpec {

	public SocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		SocksServerUri val = SocksServerUri.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		return Setting.newInstance(this, SocksServerUri.newInstance(value));
	}

}
