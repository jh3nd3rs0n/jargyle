package com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.text.Criteria;

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
