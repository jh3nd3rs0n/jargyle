package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;

final class MethodsPropertySpec extends PropertySpec<Methods> {

	public MethodsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Methods defaultVal) {
		super(permissionObj, s, Methods.class, defaultVal);
	}

	@Override
	public Property<Methods> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Methods.newInstance(value));
	}

}
