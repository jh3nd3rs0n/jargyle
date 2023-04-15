package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.text.Values;

public final class ValuesPropertySpec extends PropertySpec<Values> {

	public ValuesPropertySpec(final String n, final Values defaultVal) {
		super(n, Values.class, defaultVal);
	}

	@Override
	public Property<Values> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Values.newInstance(value));
	}

}
