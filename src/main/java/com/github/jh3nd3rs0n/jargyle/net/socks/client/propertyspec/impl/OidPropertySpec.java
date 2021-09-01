package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

final class OidPropertySpec extends PropertySpec<Oid> {

	private static Oid newOid(final String s) {
		Oid oid = null;
		try {
			oid = new Oid(s);
		} catch (GSSException e) {
			throw new IllegalArgumentException(e);
		}
		return oid;
	}
	
	public OidPropertySpec(
			final Object permissionObj, 
			final String s, 
			final String defaultVal) {
		super(permissionObj, s, Oid.class, newOid(defaultVal));
	}

	@Override
	public Property<Oid> newPropertyOfParsableValue(final String value) {
		return super.newProperty(newOid(value));
	}

}
