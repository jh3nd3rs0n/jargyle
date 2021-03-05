package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class StringPropertySpec extends PropertySpec {

	public StringPropertySpec(final String s, final String defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property getDefaultProperty() {
		return Property.newInstance(this, String.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		String val = String.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(this, value);
	}

}
