package jargyle.net.socks.server.settingspec;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class OidSettingSpec extends SettingSpec {

	public OidSettingSpec(final String s, final Oid defaultVal) {
		super(s, defaultVal);
	}
	
	@Override
	public Setting getDefaultSetting() {
		return Setting.newInstance(this, Oid.class.cast(this.defaultValue));
	}

	@Override
	public Setting newSetting(final Object value) {
		Oid val = Oid.class.cast(value);
		return Setting.newInstance(this, val);
	}

	@Override
	public Setting newSetting(final String value) {
		Oid oid = null;
		try {
			oid = new Oid(value);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return Setting.newInstance(this, oid);
	}	
	
}
