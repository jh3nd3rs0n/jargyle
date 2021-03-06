package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.AuthMethods;

public final class AuthMethodsPropertySpec extends PropertySpec {

	public AuthMethodsPropertySpec(
			final String s, final AuthMethods defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property newProperty(final Object value) {
		AuthMethods val = AuthMethods.class.cast(value);
		return super.newProperty(val);
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty(AuthMethods.newInstance(value));
	}

}
