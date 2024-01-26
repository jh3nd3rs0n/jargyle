package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;

public final class HostPropertySpec extends PropertySpec<Host> {

	public HostPropertySpec(final String n, final Host defaultVal) {
		super(n, Host.class, defaultVal);
	}

	@Override
	protected Host parse(final String value) {
		return Host.newInstance(value);
	}

}
