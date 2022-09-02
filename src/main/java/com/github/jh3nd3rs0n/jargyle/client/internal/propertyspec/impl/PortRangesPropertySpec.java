package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;

public final class PortRangesPropertySpec extends PropertySpec<PortRanges> {

	public PortRangesPropertySpec(final String s, final PortRanges defaultVal) {
		super(s, PortRanges.class, defaultVal);
	}

	@Override
	public Property<PortRanges> newPropertyOfParsableValue(final String value) {
		return super.newProperty(PortRanges.newInstance(value));
	}

}
