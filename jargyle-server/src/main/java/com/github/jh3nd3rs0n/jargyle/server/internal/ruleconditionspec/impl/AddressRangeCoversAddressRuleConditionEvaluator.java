package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionEvaluator;

public final class AddressRangeCoversAddressRuleConditionEvaluator
	extends RuleConditionEvaluator<AddressRange, String> {

	public AddressRangeCoversAddressRuleConditionEvaluator() { }

	@Override
	public boolean evaluate(final AddressRange value1, final String value2) {
		return value1.covers(value2);
	}

}
