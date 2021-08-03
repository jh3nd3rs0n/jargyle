package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

final class StringPropertySpec extends PropertySpec<String> {

	public StringPropertySpec(final String s, final String defaultVal) {
		super(s, String.class, defaultVal);
	}

	@Override
	public Property<String> newPropertyOfParsableValue(final String value) {
		return super.newProperty(value);
	}

}
