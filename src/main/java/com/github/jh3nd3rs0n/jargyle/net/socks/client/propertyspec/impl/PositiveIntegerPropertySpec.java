package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;

final class PositiveIntegerPropertySpec extends PropertySpec<PositiveInteger> {

	public PositiveIntegerPropertySpec(
			final Object permissionObj, 
			final String s, 
			final PositiveInteger defaultVal) {
		super(permissionObj, s, PositiveInteger.class, defaultVal);
	}

	@Override
	public Property<PositiveInteger> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(PositiveInteger.newInstance(value));
	}

}
