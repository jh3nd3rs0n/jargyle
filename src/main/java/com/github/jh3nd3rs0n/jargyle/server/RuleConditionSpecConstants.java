package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RuleConditionSpecConstants {

	private static final List<RuleConditionSpec<Object, Object>> VALUES;
	private static final Map<String, RuleConditionSpec<Object, Object>> VALUES_MAP;
	
	static {
		List<RuleConditionSpec<Object, Object>> values = 
				new ArrayList<RuleConditionSpec<Object, Object>>();
		values.addAll(GeneralRuleConditionSpecConstants.values());
		values.addAll(Socks5RuleConditionSpecConstants.values());
		Map<String, RuleConditionSpec<Object, Object>> valuesMap =
				new HashMap<String, RuleConditionSpec<Object, Object>>();
		valuesMap.putAll(GeneralRuleConditionSpecConstants.valuesMap());
		valuesMap.putAll(Socks5RuleConditionSpecConstants.valuesMap());
		VALUES = values;
		VALUES_MAP = valuesMap;
	}
	
	public static RuleConditionSpec<Object, Object> valueOf(final String s) {
		if (VALUES_MAP.containsKey(s)) {
			return VALUES_MAP.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown rule condition: %s", s)); 
	}
	
	public static List<RuleConditionSpec<Object, Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private RuleConditionSpecConstants() { }

}
