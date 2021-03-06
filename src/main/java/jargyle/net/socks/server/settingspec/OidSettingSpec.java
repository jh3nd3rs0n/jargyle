package jargyle.net.socks.server.settingspec;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class OidSettingSpec extends SettingSpec {

	public OidSettingSpec(final String s, final Oid defaultVal) {
		super(s, Oid.class, defaultVal);
	}

	@Override
	public Setting newSetting(final String value) {
		Oid oid = null;
		try {
			oid = new Oid(value);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newSetting(oid);
	}	
	
}
