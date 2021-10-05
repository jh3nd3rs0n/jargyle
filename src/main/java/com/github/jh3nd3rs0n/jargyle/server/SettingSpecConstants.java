package com.github.jh3nd3rs0n.jargyle.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SettingSpecConstants {

	private static final Map<String, SettingSpec<Object>> VALUES_MAP;
	
	static {
		Map<String, SettingSpec<Object>> valuesMap = 
				new HashMap<String, SettingSpec<Object>>();
		putEach(ChainingDtlsSettingSpecConstants.values(), valuesMap);
		putEach(ChainingGeneralSettingSpecConstants.values(), valuesMap);		
		putEach(ChainingSocks5SettingSpecConstants.values(), valuesMap);
		putEach(ChainingSslSettingSpecConstants.values(), valuesMap);
		putEach(DtlsSettingSpecConstants.values(), valuesMap);
		putEach(GeneralSettingSpecConstants.values(), valuesMap);
		putEach(Socks5SettingSpecConstants.values(), valuesMap);
		putEach(SslSettingSpecConstants.values(), valuesMap);
		VALUES_MAP = valuesMap;
	}
	
	private static void putEach(
			final List<SettingSpec<Object>> values,
			final Map<String, SettingSpec<Object>> valuesMap) {
		for (SettingSpec<Object> value : values) {
			valuesMap.put(value.toString(), value);
		}
	}
	
	public static SettingSpec<Object> valueOf(final String s) {
		if (VALUES_MAP.containsKey(s)) {
			return VALUES_MAP.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown setting: %s", s));		
	}
	
	private SettingSpecConstants() { }
	
}
