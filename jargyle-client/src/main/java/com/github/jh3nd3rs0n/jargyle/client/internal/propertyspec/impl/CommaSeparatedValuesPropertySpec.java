package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;

public final class CommaSeparatedValuesPropertySpec 
    extends PropertySpec<CommaSeparatedValues> {

	public CommaSeparatedValuesPropertySpec(
			final String n, final CommaSeparatedValues defaultVal) {
		super(n, CommaSeparatedValues.class, defaultVal);
	}

	@Override
	protected CommaSeparatedValues parse(final String value) {
		return CommaSeparatedValues.newInstanceFrom(value);
	}

}
