package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;

public final class Socks5MethodsPropertySpec extends PropertySpec<Methods> {

	public Socks5MethodsPropertySpec(final String n, final Methods defaultVal) {
		super(n, Methods.class, defaultVal);
	}

	@Override
	protected Methods parse(final String value) {
		return Methods.newInstanceFrom(value);
	}

}
