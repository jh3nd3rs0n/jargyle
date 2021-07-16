package jargyle.internal.net.socks.client.propertyspec.impl;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.util.Strings;

final class StringsPropertySpec extends PropertySpec<Strings> {

	public StringsPropertySpec(final String s, final Strings defaultVal) {
		super(s, Strings.class, defaultVal);
	}

	@Override
	public Property<Strings> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Strings.newInstance(value));
	}

}
