package com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.number.impl.PositiveInteger;

public final class PositiveIntegerPropertySpec 
	extends PropertySpec<PositiveInteger> {

	public PositiveIntegerPropertySpec(
			final Object permission, 
			final String s, 
			final PositiveInteger defaultVal) {
		super(permission, s, PositiveInteger.class, defaultVal);
	}

	@Override
	public Property<PositiveInteger> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(PositiveInteger.newInstance(value));
	}

}
