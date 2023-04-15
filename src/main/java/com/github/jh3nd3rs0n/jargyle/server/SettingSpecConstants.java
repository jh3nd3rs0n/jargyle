package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SettingSpecConstants {

	private static final List<SettingSpec<Object>> VALUES;
	private static final Map<String, SettingSpec<Object>> VALUES_MAP;
	
	static {
		List<SettingSpec<Object>> values = new ArrayList<SettingSpec<Object>>();
		values.addAll(ChainingDtlsSettingSpecConstants.values());
		values.addAll(ChainingGeneralSettingSpecConstants.values());		
		values.addAll(ChainingSocks5SettingSpecConstants.values());
		values.addAll(ChainingSslSettingSpecConstants.values());
		values.addAll(DtlsSettingSpecConstants.values());
		values.addAll(GeneralSettingSpecConstants.values());
		values.addAll(Socks5SettingSpecConstants.values());
		values.addAll(SslSettingSpecConstants.values());
		Map<String, SettingSpec<Object>> valuesMap = 
				new HashMap<String, SettingSpec<Object>>();
		valuesMap.putAll(ChainingDtlsSettingSpecConstants.valuesMap());
		valuesMap.putAll(ChainingGeneralSettingSpecConstants.valuesMap());		
		valuesMap.putAll(ChainingSocks5SettingSpecConstants.valuesMap());
		valuesMap.putAll(ChainingSslSettingSpecConstants.valuesMap());
		valuesMap.putAll(DtlsSettingSpecConstants.valuesMap());
		valuesMap.putAll(GeneralSettingSpecConstants.valuesMap());
		valuesMap.putAll(Socks5SettingSpecConstants.valuesMap());
		valuesMap.putAll(SslSettingSpecConstants.valuesMap());		
		VALUES = values;
		VALUES_MAP = valuesMap;
	}
	
	public static SettingSpec<Object> valueOfName(final String name) {
		if (VALUES_MAP.containsKey(name)) {
			return VALUES_MAP.get(name);
		}
		throw new IllegalArgumentException(String.format(
				"unknown setting: %s", name));		
	}
	
	public static List<SettingSpec<Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private SettingSpecConstants() { }
	
}
