package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class BooleanPropertySpec extends PropertySpec {

	public BooleanPropertySpec(final String s, final Boolean defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property newProperty(final Object value) {
		Boolean val = Boolean.class.cast(value);
		return super.newProperty(val);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty(Boolean.valueOf(value));
	}

}
