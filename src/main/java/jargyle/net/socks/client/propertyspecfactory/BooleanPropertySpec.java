package jargyle.net.socks.client.propertyspecfactory;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

final class BooleanPropertySpec extends PropertySpec<Boolean> {

	public BooleanPropertySpec(final String s, final Boolean defaultVal) {
		super(s, Boolean.class, defaultVal);
	}

	@Override
	public Property<Boolean> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Boolean.valueOf(value));
	}

}
