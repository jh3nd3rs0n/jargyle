package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class BooleanPropertySpec extends PropertySpec {

	public BooleanPropertySpec(final String s, final Boolean defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property getDefaultProperty() {
		return Property.newInstance(
				this, Boolean.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		Boolean val = Boolean.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(this, Boolean.valueOf(value));
	}

}
