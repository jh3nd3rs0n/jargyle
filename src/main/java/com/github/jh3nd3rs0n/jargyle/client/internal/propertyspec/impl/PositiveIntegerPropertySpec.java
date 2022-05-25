package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;

public final class PositiveIntegerPropertySpec 
	extends PropertySpec<PositiveInteger> {

	public PositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal) {
		super(s, PositiveInteger.class, defaultVal);
	}

	@Override
	public Property<PositiveInteger> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(PositiveInteger.newInstance(value));
	}

}
