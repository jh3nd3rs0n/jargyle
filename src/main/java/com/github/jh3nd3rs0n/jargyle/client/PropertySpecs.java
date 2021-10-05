package com.github.jh3nd3rs0n.jargyle.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class PropertySpecs {

	private final List<PropertySpec<Object>> propertySpecs;
	
	public PropertySpecs() {
		this.propertySpecs = new ArrayList<PropertySpec<Object>>();
	}
	
	public <T> PropertySpec<T> addThenGet(final PropertySpec<T> value) {
		@SuppressWarnings("unchecked")
		PropertySpec<Object> val = (PropertySpec<Object>) value;
		this.propertySpecs.add(val);
		return value;
	}
	
	public List<PropertySpec<Object>> toList() {
		return Collections.unmodifiableList(this.propertySpecs);
	}
	
}
