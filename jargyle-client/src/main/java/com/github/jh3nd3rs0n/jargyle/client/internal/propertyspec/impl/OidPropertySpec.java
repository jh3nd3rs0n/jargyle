package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

public final class OidPropertySpec extends PropertySpec<Oid> {

	private static Oid newOid(final String s) {
		Oid oid = null;
		try {
			oid = new Oid(s);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return oid;
	}
	
	public OidPropertySpec(final String n, final String defaultVal) {
		super(n, Oid.class, newOid(defaultVal));
	}

	@Override
	protected Oid parse(final String value) {
		return newOid(value);
	}

}
