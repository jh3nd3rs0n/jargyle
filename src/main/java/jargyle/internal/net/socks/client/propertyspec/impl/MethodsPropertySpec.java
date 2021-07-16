package jargyle.internal.net.socks.client.propertyspec.impl;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.Methods;

final class MethodsPropertySpec extends PropertySpec<Methods> {

	public MethodsPropertySpec(final String s, final Methods defaultVal) {
		super(s, Methods.class, defaultVal);
	}

	@Override
	public Property<Methods> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Methods.newInstance(value));
	}

}
