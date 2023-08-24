package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

public final class GeneralRuleArgSpecConstants {

	private static final RuleArgSpecs RULE_ARG_SPECS = new RuleArgSpecs();
	
	public static final RuleArgSpec<String> CLIENT_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"clientAddress", 
			String.class));
	
	public static final RuleArgSpec<String> SOCKS_SERVER_ADDRESS = RULE_ARG_SPECS.addThenGet(new RuleArgSpec<String>(
			"socksServerAddress", 
			String.class));
	
	public static List<RuleArgSpec<Object>> values() {
		return RULE_ARG_SPECS.toList();
	}
	
	public static Map<String, RuleArgSpec<Object>> valuesMap() {
		return RULE_ARG_SPECS.toMap();
	}
	
	private GeneralRuleArgSpecConstants() { }

}
