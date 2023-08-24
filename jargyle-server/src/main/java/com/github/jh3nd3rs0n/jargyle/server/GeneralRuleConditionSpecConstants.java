package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.AddressRangeContainsAddressRuleConditionSpec;

public final class GeneralRuleConditionSpecConstants {

	private static final RuleConditionSpecs RULE_CONDITION_SPECS = new RuleConditionSpecs();
	
	@HelpText(
			doc = "Specifies the client address",
			usage = "clientAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)	
	public static final RuleConditionSpec<AddressRange, String> CLIENT_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"clientAddress",
			GeneralRuleArgSpecConstants.CLIENT_ADDRESS));
	
	@HelpText(
			doc = "Specifies the SOCKS server address the client connected to",
			usage = "socksServerAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS_SERVER_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeContainsAddressRuleConditionSpec(
			"socksServerAddress",
			GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS));
	
	public static List<RuleConditionSpec<Object, Object>> values() {
		return RULE_CONDITION_SPECS.toList();
	}
	
	public static Map<String, RuleConditionSpec<Object, Object>> valuesMap() {
		return RULE_CONDITION_SPECS.toMap();
	}
	
	private GeneralRuleConditionSpecConstants() { }

}
