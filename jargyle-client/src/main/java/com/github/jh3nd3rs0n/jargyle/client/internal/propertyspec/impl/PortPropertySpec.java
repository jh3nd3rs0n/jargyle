package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;

public final class PortPropertySpec extends PropertySpec<Port> {

	public PortPropertySpec(final String n, final Port defaultVal) {
		super(n, Port.class, defaultVal);
	}

	@Override
	public Property<Port> newPropertyWithParsableValue(final String value) {
		return this.newProperty(Port.newInstance(value));
	}

}
