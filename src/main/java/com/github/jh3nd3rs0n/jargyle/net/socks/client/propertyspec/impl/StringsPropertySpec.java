package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

final class StringsPropertySpec extends PropertySpec<Strings> {

	public StringsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Strings defaultVal) {
		super(permissionObj, s, Strings.class, defaultVal);
	}

	@Override
	public Property<Strings> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Strings.newInstance(value));
	}

}
