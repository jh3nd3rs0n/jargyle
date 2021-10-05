package com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl;

import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;

public final class HostPropertySpec extends PropertySpec<Host> {

	public HostPropertySpec(
			final Object permission, 
			final String s, 
			final Host defaultVal) {
		super(permission, s, Host.class, defaultVal);
	}

	@Override
	public Property<Host> newPropertyOfParsableValue(final String value) {
		Host host = null;
		try {
			host = Host.newInstance(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newProperty(host);
	}

}
