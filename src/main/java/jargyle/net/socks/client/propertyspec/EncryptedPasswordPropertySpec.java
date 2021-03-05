package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.security.EncryptedPassword;

public final class EncryptedPasswordPropertySpec extends PropertySpec {

	public EncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		super(s, defaultVal);
	}

	@Override
	public Property getDefaultProperty() {
		return Property.newInstance(
				this, EncryptedPassword.class.cast(this.defaultValue));
	}

	@Override
	public Property newProperty(final Object value) {
		EncryptedPassword val = EncryptedPassword.class.cast(value);
		return Property.newInstance(this, val);
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(
				this, EncryptedPassword.newInstance(value.toCharArray()));
	}

}
