package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

final class HostPropertySpec extends PropertySpec<Host> {

	public HostPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Host defaultVal) {
		super(permissionObj, s, Host.class, defaultVal);
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
