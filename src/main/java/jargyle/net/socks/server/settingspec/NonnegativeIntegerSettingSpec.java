package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;
import jargyle.util.NonnegativeInteger;

public final class NonnegativeIntegerSettingSpec extends SettingSpec {

	public NonnegativeIntegerSettingSpec(
			final String s, final NonnegativeInteger defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Setting newSetting(final Object value) {
		NonnegativeInteger val = NonnegativeInteger.class.cast(value);
		return super.newSetting(val);
	}

	@Override
	public Setting newSetting(final String value) {
		return super.newSetting(NonnegativeInteger.newInstance(value));
	}

}
