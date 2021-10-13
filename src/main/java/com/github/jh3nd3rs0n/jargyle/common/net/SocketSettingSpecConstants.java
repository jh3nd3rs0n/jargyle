package com.github.jh3nd3rs0n.jargyle.common.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SocketSettingSpecConstants {

	private static final List<SocketSettingSpec<Object>> VALUES;
	private static final Map<String, SocketSettingSpec<Object>> VALUES_MAP;
	
	static {
		List<SocketSettingSpec<Object>> values = 
				new ArrayList<SocketSettingSpec<Object>>();
		values.addAll(StandardSocketSettingSpecConstants.values());
		Map<String, SocketSettingSpec<Object>> valuesMap = 
				new HashMap<String, SocketSettingSpec<Object>>();
		for (SocketSettingSpec<Object> value : values) {
			valuesMap.put(value.toString(), value);
		}
		VALUES = values;
		VALUES_MAP = valuesMap;
	}
	
	public static SocketSettingSpec<Object> valueOf(final String s) {
		if (VALUES_MAP.containsKey(s)) {
			return VALUES_MAP.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown socket setting: %s", s));
	}
	
	public static List<SocketSettingSpec<Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private SocketSettingSpecConstants() { }
	
}
