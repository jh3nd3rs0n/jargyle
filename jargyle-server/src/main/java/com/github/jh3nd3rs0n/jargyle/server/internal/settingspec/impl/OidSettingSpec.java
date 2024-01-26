package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

public final class OidSettingSpec extends SettingSpec<Oid> {

	public OidSettingSpec(final String n, final Oid defaultVal) {
		super(n, Oid.class, defaultVal);
	}

	@Override
	protected Oid parse(final String value) {
		Oid oid;
		try {
			oid = new Oid(value);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return oid;
	}

}
