package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.util.PositiveInteger;

public final class PositiveIntegerPropertySpec extends PropertySpec {

	public PositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property getDefaultProperty() {
		return Property.newInstance(
				this, PositiveInteger.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		PositiveInteger val = PositiveInteger.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(this, PositiveInteger.newInstance(value));
	}

}
