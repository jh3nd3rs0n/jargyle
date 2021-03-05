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
	public Property getDefaultProperty() {
		return Property.newInstance(
				this, AuthMethods.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		AuthMethods val = AuthMethods.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(this, AuthMethods.newInstance(value));
	}

}
