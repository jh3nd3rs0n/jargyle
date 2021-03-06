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
	public Property newProperty(final Object value) {
		PositiveInteger val = PositiveInteger.class.cast(value);
		return super.newProperty(val);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty(PositiveInteger.newInstance(value));
	}

}
