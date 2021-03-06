package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.v5.UsernamePassword;

public final class UsernamePropertySpec extends PropertySpec {

	private static String getValidatedUsername(final String s) {
		UsernamePassword.validateUsername(s);
		return s;
	}
	
	public UsernamePropertySpec(final String s, final String defaultVal) {
		super(s, getValidatedUsername(defaultVal));
	}

	@Override
	public Property newProperty(final Object value) {
		String val = String.class.cast(value);
		return super.newProperty((Object) getValidatedUsername(val));
	}

	@Override
	public Property newProperty(final String value) {
		return super.newProperty((Object) getValidatedUsername(value));
	}

}
