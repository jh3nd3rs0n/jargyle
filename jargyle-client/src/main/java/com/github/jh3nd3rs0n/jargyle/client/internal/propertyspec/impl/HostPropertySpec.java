package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;

public final class HostPropertySpec extends PropertySpec<Host> {

	public HostPropertySpec(final String n, final Host defaultVal) {
		super(n, Host.class, defaultVal);
	}

	@Override
	public Property<Host> newPropertyWithParsableValue(final String value) {
		return super.newProperty(Host.newInstance(value));
	}

}
