package jargyle.net.socks.client.propertyspec.impl;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

final class StringPropertySpec extends PropertySpec<String> {

	public StringPropertySpec(final String s, final String defaultVal) {
		super(s, String.class, defaultVal);
	}

	@Override
	public Property<String> newPropertyOfParsableValue(final String value) {
		return super.newProperty(value);
	}

}
