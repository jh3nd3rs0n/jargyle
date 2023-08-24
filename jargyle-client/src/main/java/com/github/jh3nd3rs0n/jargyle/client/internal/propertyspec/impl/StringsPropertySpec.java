package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.lang.Strings;

public final class StringsPropertySpec extends PropertySpec<Strings> {

	public StringsPropertySpec(final String n, final Strings defaultVal) {
		super(n, Strings.class, defaultVal);
	}

	@Override
	public Property<Strings> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Strings.newInstance(value));
	}

}
