package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

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
	public Property<Oid> newPropertyWithParsableValue(final String value) {
		return super.newProperty(newOid(value));
	}

}
