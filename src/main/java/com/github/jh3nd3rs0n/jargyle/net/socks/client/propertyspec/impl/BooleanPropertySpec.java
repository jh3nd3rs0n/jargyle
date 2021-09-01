package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

final class BooleanPropertySpec extends PropertySpec<Boolean> {

	public BooleanPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Boolean defaultVal) {
		super(permissionObj, s, Boolean.class, defaultVal);
	}

	@Override
	public Property<Boolean> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Boolean.valueOf(value));
	}

}
