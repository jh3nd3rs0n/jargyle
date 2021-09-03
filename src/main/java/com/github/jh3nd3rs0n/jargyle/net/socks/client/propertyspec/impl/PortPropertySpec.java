package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

public final class PortPropertySpec extends PropertySpec<Port> {

	public PortPropertySpec(
			final Object permission, 
			final String s, 
			final Port defaultVal) {
		super(permission, s, Port.class, defaultVal);
	}

	@Override
	public Property<Port> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Port.newInstance(value));
	}

}
