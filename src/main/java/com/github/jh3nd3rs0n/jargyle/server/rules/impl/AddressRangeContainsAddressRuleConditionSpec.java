package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;

public final class AddressRangeContainsAddressRuleConditionSpec 
	extends RuleConditionSpec<AddressRange, String> {

	public AddressRangeContainsAddressRuleConditionSpec(
			final String s, final RuleArgSpec<String> rlArgSpec) {
		super(
				s, 
				AddressRange.class, 
				new AddressRangeContainsAddressRuleConditionEvaluator(), 
				rlArgSpec);
	}

	@Override
	public RuleCondition<AddressRange, String> newRuleConditionOfParsableValue(
			final String value) {
		return super.newRuleCondition(AddressRange.newInstance(value));
	}

}
