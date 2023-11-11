package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;

public final class CommaSeparatedValuesPropertySpec 
    extends PropertySpec<CommaSeparatedValues> {

	public CommaSeparatedValuesPropertySpec(
			final String n, final CommaSeparatedValues defaultVal) {
		super(n, CommaSeparatedValues.class, defaultVal);
	}

	@Override
	public Property<CommaSeparatedValues> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(CommaSeparatedValues.newInstance(value));
	}

}
