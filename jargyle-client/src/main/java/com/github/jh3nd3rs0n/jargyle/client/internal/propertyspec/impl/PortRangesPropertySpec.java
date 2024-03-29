package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;

public final class PortRangesPropertySpec extends PropertySpec<PortRanges> {

	public PortRangesPropertySpec(
			final String n, final PortRanges defaultVal) {
		super(n, PortRanges.class, defaultVal);
	}

	@Override
	protected PortRanges parse(final String value) {
		return PortRanges.newInstanceFrom(value);
	}

}
