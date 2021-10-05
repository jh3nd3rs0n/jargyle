package com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.text.Strings;

public final class StringsPropertySpec extends PropertySpec<Strings> {

	public StringsPropertySpec(
			final Object permission, 
			final String s, 
			final Strings defaultVal) {
		super(permission, s, Strings.class, defaultVal);
	}

	@Override
	public Property<Strings> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Strings.newInstance(value));
	}

}
