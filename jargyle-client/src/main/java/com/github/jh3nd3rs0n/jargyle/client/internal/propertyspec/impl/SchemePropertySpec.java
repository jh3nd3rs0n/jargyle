package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;

public final class SchemePropertySpec extends PropertySpec<Scheme> {

	public SchemePropertySpec(final String n, final Scheme defaultVal) {
		super(n, Scheme.class, defaultVal);
	}

	@Override
	protected Scheme parse(final String value) {
		return Scheme.valueOfString(value);
	}

}
