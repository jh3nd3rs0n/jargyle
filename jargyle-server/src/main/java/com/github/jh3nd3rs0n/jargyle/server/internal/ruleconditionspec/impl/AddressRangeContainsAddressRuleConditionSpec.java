package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;

public final class AddressRangeContainsAddressRuleConditionSpec 
	extends RuleConditionSpec<AddressRange, String> {

	public AddressRangeContainsAddressRuleConditionSpec(
			final String n, final RuleArgSpec<String> rlArgSpec) {
		super(
				n, 
				AddressRange.class, 
				new AddressRangeContainsAddressRuleConditionEvaluator(), 
				rlArgSpec);
	}

	@Override
	public RuleCondition<AddressRange, String> newRuleConditionWithParsedValue(
			final String value) {
		return super.newRuleCondition(AddressRange.newInstanceFrom(value));
	}

}
