package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl.AddressRangeCoversAddressRuleConditionSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "General Rule Conditions"
)
public final class GeneralRuleConditionSpecConstants {

	private static final RuleConditionSpecs RULE_CONDITION_SPECS = new RuleConditionSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the client address",
			name = "clientAddress",
			syntax = "clientAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)	
	public static final RuleConditionSpec<AddressRange, String> CLIENT_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
			"clientAddress",
			GeneralRuleArgSpecConstants.CLIENT_ADDRESS));
	
	@NameValuePairValueSpecDoc(
			description = "Specifies the SOCKS server address the client "
					+ "connected to",
			name = "socksServerAddress",
			syntax = "socksServerAddress=ADDRESS_RANGE",
			valueType = AddressRange.class
	)	
	public static final RuleConditionSpec<AddressRange, String> SOCKS_SERVER_ADDRESS = RULE_CONDITION_SPECS.addThenGet(new AddressRangeCoversAddressRuleConditionSpec(
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
