package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.util.HashMap;
import java.util.Map;

final class SettingSpecConstants {

	private static final Map<String, SettingSpec<Object>> VALUES_MAP;
	
	static {
		Map<String, SettingSpec<Object>> valuesMap = 
				new HashMap<String, SettingSpec<Object>>();
		valuesMap.putAll(ChainingDtlsSettingSpecConstants.valuesMap());
		valuesMap.putAll(ChainingSocks5SettingSpecConstants.valuesMap());
		valuesMap.putAll(ChainingSslSettingSpecConstants.valuesMap());
		valuesMap.putAll(ChainingGeneralSettingSpecConstants.valuesMap());
		valuesMap.putAll(DtlsSettingSpecConstants.valuesMap());
		valuesMap.putAll(Socks5SettingSpecConstants.valuesMap());
		valuesMap.putAll(SslSettingSpecConstants.valuesMap());
		valuesMap.putAll(GeneralSettingSpecConstants.valuesMap());
		VALUES_MAP = valuesMap;
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
