package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class OidSettingSpec extends SettingSpec<Oid> {

	public OidSettingSpec(final String n, final Oid defaultVal) {
		super(n, Oid.class, defaultVal);
	}

	@Override
	public Setting<Oid> newSettingWithParsableValue(final String value) {
		Oid oid = null;
		try {
			oid = new Oid(value);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newSetting(oid);
	}	
	
}
