package com.github.jh3nd3rs0n.jargyle.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class PropertySpecs {

	private final List<PropertySpec<Object>> propertySpecs;
	private final Map<String, PropertySpec<Object>> propertySpecsMap;
	
	public PropertySpecs() {
		this.propertySpecs = new ArrayList<PropertySpec<Object>>();
		this.propertySpecsMap = new HashMap<String, PropertySpec<Object>>();
	}
	
	public <T> PropertySpec<T> addThenGet(final PropertySpec<T> value) {
		@SuppressWarnings("unchecked")
		PropertySpec<Object> val = (PropertySpec<Object>) value;
		this.propertySpecs.add(val);
		this.propertySpecsMap.put(val.getName(), val);
		return value;
	}
	
	public List<PropertySpec<Object>> toList() {
		return Collections.unmodifiableList(this.propertySpecs);
	}
	
	public Map<String, PropertySpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.propertySpecsMap);
	}
	
}
