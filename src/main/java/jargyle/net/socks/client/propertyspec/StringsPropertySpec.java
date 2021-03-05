package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.util.Strings;

public final class StringsPropertySpec extends PropertySpec {

	public StringsPropertySpec(final String s, final Strings defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property getDefaultProperty() {
		return Property.newInstance(
				this, Strings.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		Strings val = Strings.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(this, Strings.newInstance(value));
	}

}
