package com.github.jh3nd3rs0n.jargyle.common.net;

import java.util.HashMap;
import java.util.Map;

final class SocketSettingSpecConstants {

	private static final Map<String, SocketSettingSpec<Object>> VALUES_MAP;
	
	static {
		Map<String, SocketSettingSpec<Object>> valuesMap = 
				new HashMap<String, SocketSettingSpec<Object>>();
		for (SocketSettingSpec<Object> value 
				: StandardSocketSettingSpecConstants.values()) {
			valuesMap.put(value.toString(), value);
		}
		VALUES_MAP = valuesMap;
	}
	
	public static SocketSettingSpec<Object> valueOf(final String s) {
		if (VALUES_MAP.containsKey(s)) {
			return VALUES_MAP.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown socket setting: %s", s));
	}
	
	private SocketSettingSpecConstants() { }
	
}
