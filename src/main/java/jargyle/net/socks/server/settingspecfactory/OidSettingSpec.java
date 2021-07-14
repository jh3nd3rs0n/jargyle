package jargyle.net.socks.server.settingspecfactory;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

final class OidSettingSpec extends SettingSpec<Oid> {

	public OidSettingSpec(final String s, final Oid defaultVal) {
		super(s, Oid.class, defaultVal);
	}

	@Override
	public Setting<Oid> newSettingOfParsableValue(final String value) {
		Oid oid = null;
		try {
			oid = new Oid(value);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newSetting(oid);
	}	
	
}
