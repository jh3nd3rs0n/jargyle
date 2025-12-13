package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RuleActionSpecConstants {

	private static final List<RuleActionSpec<Object>> VALUES;
	private static final Map<String, RuleActionSpec<Object>> VALUES_MAP;
	
	static {
		List<RuleActionSpec<Object>> values =
				new ArrayList<RuleActionSpec<Object>>();
		values.addAll(GeneralRuleActionSpecConstants.values());
        values.addAll(SocksRuleActionSpecConstants.values());
		values.addAll(Socks5RuleActionSpecConstants.values());
		Map<String, RuleActionSpec<Object>> valuesMap =
				new HashMap<String, RuleActionSpec<Object>>();
		valuesMap.putAll(GeneralRuleActionSpecConstants.valuesMap());
        valuesMap.putAll(SocksRuleActionSpecConstants.valuesMap());
		valuesMap.putAll(Socks5RuleActionSpecConstants.valuesMap());
		VALUES = values;
		VALUES_MAP = valuesMap;
	}
	
	public static RuleActionSpec<Object> valueOfName(final String name) {
		if (VALUES_MAP.containsKey(name)) {
			return VALUES_MAP.get(name);
		}
		throw new IllegalArgumentException(String.format(
				"unknown rule action name: %s", name));
	}
	
	public static List<RuleActionSpec<Object>> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private RuleActionSpecConstants() { }

}
