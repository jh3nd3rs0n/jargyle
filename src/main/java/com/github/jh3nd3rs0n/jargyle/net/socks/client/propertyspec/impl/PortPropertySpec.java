package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

final class PortPropertySpec extends PropertySpec<Port> {

	public PortPropertySpec(final String s, final Port defaultVal) {
		super(s, Port.class, defaultVal);
	}

	@Override
	public Property<Port> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Port.newInstance(value));
	}

}
