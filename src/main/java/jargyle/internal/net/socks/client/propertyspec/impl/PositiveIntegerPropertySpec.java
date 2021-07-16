package jargyle.internal.net.socks.client.propertyspec.impl;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.util.PositiveInteger;

final class PositiveIntegerPropertySpec 
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
