package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
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
	protected AddressRange parse(final String value) {
		return AddressRange.newInstanceFrom(value);
	}

}
