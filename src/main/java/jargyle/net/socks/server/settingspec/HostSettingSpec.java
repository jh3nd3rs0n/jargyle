package jargyle.net.socks.server.settingspec;

import java.net.UnknownHostException;

import jargyle.net.Host;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class HostSettingSpec extends SettingSpec {

	public HostSettingSpec(final String s, final Host defaultVal) {
		super(s, Host.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		Host host = null;
		try {
			host = Host.newInstance(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newSetting(host);
	}

}
