package com.github.jh3nd3rs0n.jargyle.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class PropertySpecConstants {

	private static final List<PropertySpec<Object>> VALUES;
	private static final Map<String, PropertySpec<Object>> VALUES_MAP;
	
	static {
		List<PropertySpec<Object>> values = 
				new ArrayList<PropertySpec<Object>>();
		values.addAll(SocksServerUriPropertySpecConstants.values());
		values.addAll(SocksClientPropertySpecConstants.values());
		Map<String, PropertySpec<Object>> valuesMap = 
				new HashMap<String, PropertySpec<Object>>();
		valuesMap.putAll(SocksServerUriPropertySpecConstants.valuesMap());
		valuesMap.putAll(SocksClientPropertySpecConstants.valuesMap());
		VALUES = values;
		VALUES_MAP = valuesMap;
	}
	
	public static PropertySpec<Object> valueOfName(final String name) {
		if (VALUES_MAP.containsKey(name)) {
			return VALUES_MAP.get(name);
		}
		throw new IllegalArgumentException(String.format(
				"unknown property name: %s", name));
	}
	
	public static List<PropertySpec<Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private PropertySpecConstants() { }
	
}
