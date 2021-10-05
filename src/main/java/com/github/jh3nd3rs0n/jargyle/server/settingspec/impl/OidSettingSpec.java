package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class OidSettingSpec extends SettingSpec<Oid> {

	public OidSettingSpec(
			final Object permission, 
			final String s, 
			final Oid defaultVal) {
		super(permission, s, Oid.class, defaultVal);
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
