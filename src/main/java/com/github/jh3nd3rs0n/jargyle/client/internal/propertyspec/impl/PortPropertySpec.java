package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;

public final class PortPropertySpec extends PropertySpec<Port> {

	public PortPropertySpec(final String s, final Port defaultVal) {
		super(s, Port.class, defaultVal);
	}

	@Override
	public Property<Port> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Port.newInstance(value));
	}

}
