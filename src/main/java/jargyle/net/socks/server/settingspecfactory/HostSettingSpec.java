package jargyle.net.socks.server.settingspecfactory;

import java.net.UnknownHostException;

import jargyle.net.Host;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

final class HostSettingSpec extends SettingSpec<Host> {

	public HostSettingSpec(final String s, final Host defaultVal) {
		super(s, Host.class, defaultVal);
	}

	@Override
	public Setting<Host> newSettingOfParsableValue(final String value) {
		Host host = null;
		try {
			host = Host.newInstance(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newSetting(host);
	}

}
