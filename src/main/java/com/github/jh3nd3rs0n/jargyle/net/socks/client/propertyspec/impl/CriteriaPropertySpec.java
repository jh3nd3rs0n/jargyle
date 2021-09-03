package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;

public final class CriteriaPropertySpec extends PropertySpec<Criteria> {

	public CriteriaPropertySpec(
			final Object permission, 
			final String s, 
			final Criteria defaultVal) {
		super(permission, s, Criteria.class, defaultVal);
	}

	@Override
	public Property<Criteria> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Criteria.newInstance(value));
	}
	
}
