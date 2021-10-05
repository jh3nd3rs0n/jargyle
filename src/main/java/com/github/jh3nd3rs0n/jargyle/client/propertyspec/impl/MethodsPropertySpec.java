package com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public final class MethodsPropertySpec extends PropertySpec<Methods> {

	public MethodsPropertySpec(
			final Object permission, 
			final String s, 
			final Methods defaultVal) {
		super(permission, s, Methods.class, defaultVal);
	}

	@Override
	public Property<Methods> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Methods.newInstance(value));
	}

}
