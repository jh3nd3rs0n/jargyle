package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.util.Strings;

public final class StringsPropertySpec extends PropertySpec {

	public StringsPropertySpec(final String s, final Strings defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property newProperty(final Object value) {
		Strings val = Strings.class.cast(value);
		return super.newProperty(val);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty(Strings.newInstance(value));
	}

}
