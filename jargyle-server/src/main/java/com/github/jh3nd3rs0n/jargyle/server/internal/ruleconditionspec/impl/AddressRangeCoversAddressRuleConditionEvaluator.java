package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionEvaluator;

public final class AddressRangeCoversAddressRuleConditionEvaluator
	extends RuleConditionEvaluator<AddressRange, String> {

	public AddressRangeCoversAddressRuleConditionEvaluator() { }

	@Override
	public boolean evaluate(final AddressRange conditionValue, final String argValue) {
		return conditionValue.covers(argValue);
	}

}
