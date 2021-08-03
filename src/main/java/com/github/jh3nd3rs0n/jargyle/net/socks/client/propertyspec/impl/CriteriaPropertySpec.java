package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;

final class CriteriaPropertySpec extends PropertySpec<Criteria> {

	public CriteriaPropertySpec(final String s, final Criteria defaultVal) {
		super(s, Criteria.class, defaultVal);
	}

	@Override
	public Property<Criteria> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Criteria.newInstance(value));
	}
	
}
