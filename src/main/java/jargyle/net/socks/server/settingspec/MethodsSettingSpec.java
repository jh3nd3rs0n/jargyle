package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.transport.v5.Methods;

public final class MethodsSettingSpec extends SettingSpec<Methods> {

	public MethodsSettingSpec(final String s, final Methods defaultVal) {
		super(s, Methods.class, defaultVal);
	}

	@Override
	public Setting<Methods> newSettingOfParsableValue(final String value) {
		return super.newSetting(Methods.newInstance(value));
	}
	
}
