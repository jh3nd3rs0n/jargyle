package jargyle.net.socks.client.propertyspec.impl;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;

final class UsernamePropertySpec extends PropertySpec<String> {

	private static String getValidatedUsername(final String s) {
		UsernamePassword.validateUsername(s);
		return s;
	}
	
	public UsernamePropertySpec(final String s, final String defaultVal) {
		super(s, String.class, getValidatedUsername(defaultVal));
	}

	@Override
	public Property<String> newProperty(final String value) {
		return super.newProperty(getValidatedUsername(value));
	}

	@Override
	public Property<String> newPropertyOfParsableValue(final String value) {
		return super.newProperty(getValidatedUsername(value));
	}

}
