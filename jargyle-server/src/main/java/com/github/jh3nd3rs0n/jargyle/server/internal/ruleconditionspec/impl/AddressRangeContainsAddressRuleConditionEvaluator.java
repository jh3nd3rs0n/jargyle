package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionEvaluator;

public final class AddressRangeContainsAddressRuleConditionEvaluator 
	extends RuleConditionEvaluator<AddressRange, String> {

	public AddressRangeContainsAddressRuleConditionEvaluator() { }

	@Override
	public boolean evaluate(final AddressRange value1, final String value2) {
		return value1.has(value2);
	}

}
