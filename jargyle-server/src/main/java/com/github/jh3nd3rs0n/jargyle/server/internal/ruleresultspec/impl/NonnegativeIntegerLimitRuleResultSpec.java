package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.NonNegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class NonnegativeIntegerLimitRuleResultSpec 
	extends RuleResultSpec<NonNegativeIntegerLimit> {

	public NonnegativeIntegerLimitRuleResultSpec(final String n) {
		super(n, NonNegativeIntegerLimit.class);
	}

	@Override
	public RuleResult<NonNegativeIntegerLimit> newRuleResultWithParsedValue(
			final String value) {
		return super.newRuleResult(NonNegativeIntegerLimit.newInstanceFrom(value));
	}

}
