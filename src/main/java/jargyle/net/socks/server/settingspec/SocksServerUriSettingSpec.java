package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class SocksServerUriSettingSpec extends SettingSpec {

	public SocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		super(s, SocksServerUri.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(SocksServerUri.newInstance(value));
	}

}
