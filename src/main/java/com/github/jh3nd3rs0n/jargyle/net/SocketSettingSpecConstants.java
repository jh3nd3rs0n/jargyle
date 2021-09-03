package com.github.jh3nd3rs0n.jargyle.net;

import java.util.Map;

final class SocketSettingSpecConstants {

	private static final Map<String, SocketSettingSpec<Object>> VALUES_MAP =
			StandardSocketSettingSpecConstants.valuesMap();
	
	public static SocketSettingSpec<Object> valueOf(final String s) {
		if (VALUES_MAP.containsKey(s)) {
			return VALUES_MAP.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown socket setting: %s", s));
	}
	
	private SocketSettingSpecConstants() { }
	
}
