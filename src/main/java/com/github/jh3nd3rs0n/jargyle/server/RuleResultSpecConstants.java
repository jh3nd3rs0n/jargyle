package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RuleResultSpecConstants {

	private static final List<RuleResultSpec<Object>> VALUES;
	private static final Map<String, RuleResultSpec<Object>> VALUES_MAP;
	
	static {
		List<RuleResultSpec<Object>> values = 
				new ArrayList<RuleResultSpec<Object>>();
		values.addAll(GeneralRuleResultSpecConstants.values());
		values.addAll(Socks5RuleResultSpecConstants.values());
		Map<String, RuleResultSpec<Object>> valuesMap =
				new HashMap<String, RuleResultSpec<Object>>();
		valuesMap.putAll(GeneralRuleResultSpecConstants.valuesMap());
		valuesMap.putAll(Socks5RuleResultSpecConstants.valuesMap());
		VALUES = values;
		VALUES_MAP = valuesMap;
	}
	
	public static RuleResultSpec<Object> valueOf(final String s) {
		if (VALUES_MAP.containsKey(s)) {
			return VALUES_MAP.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown rule result: %s", s));
	}
	
	public static List<RuleResultSpec<Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private RuleResultSpecConstants() { }

}
