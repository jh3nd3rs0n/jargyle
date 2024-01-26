package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

public final class BooleanPropertySpec extends PropertySpec<Boolean> {

	public BooleanPropertySpec(final String n, final Boolean defaultVal) {
		super(n, Boolean.class, defaultVal);
	}

	@Override
	protected Boolean parse(final String value) {
		return Boolean.valueOf(value);
	}

}
