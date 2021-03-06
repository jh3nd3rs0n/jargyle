package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class StringPropertySpec extends PropertySpec {

	public StringPropertySpec(final String s, final String defaultVal) {
		super(s, String.class, defaultVal);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty((Object) value);
	}

}
