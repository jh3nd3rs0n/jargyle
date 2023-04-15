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
		values.addAll(DtlsPropertySpecConstants.values());
		values.addAll(GeneralPropertySpecConstants.values());
		values.addAll(Socks5PropertySpecConstants.values());
		values.addAll(SslPropertySpecConstants.values());
		Map<String, PropertySpec<Object>> valuesMap = 
				new HashMap<String, PropertySpec<Object>>();
		valuesMap.putAll(DtlsPropertySpecConstants.valuesMap());
		valuesMap.putAll(GeneralPropertySpecConstants.valuesMap());
		valuesMap.putAll(Socks5PropertySpecConstants.valuesMap());
		valuesMap.putAll(SslPropertySpecConstants.valuesMap());		
		VALUES = values;
		VALUES_MAP = valuesMap;
	}
	
	public static PropertySpec<Object> valueOfName(final String name) {
		if (VALUES_MAP.containsKey(name)) {
			return VALUES_MAP.get(name);
		}
		throw new IllegalArgumentException(String.format(
				"unknown property: %s", name));
	}
	
	public static List<PropertySpec<Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private PropertySpecConstants() { }
	
}
