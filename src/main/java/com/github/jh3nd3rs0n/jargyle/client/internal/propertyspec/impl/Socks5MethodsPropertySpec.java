package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public final class Socks5MethodsPropertySpec extends PropertySpec<Methods> {

	public Socks5MethodsPropertySpec(final String n, final Methods defaultVal) {
		super(n, Methods.class, defaultVal);
	}

	@Override
	public Property<Methods> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Methods.newInstance(value));
	}

}
