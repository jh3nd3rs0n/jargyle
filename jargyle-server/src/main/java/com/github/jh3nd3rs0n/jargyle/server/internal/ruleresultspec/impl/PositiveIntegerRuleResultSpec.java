package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class PositiveIntegerRuleResultSpec 
	extends RuleResultSpec<PositiveInteger> {

	public PositiveIntegerRuleResultSpec(final String n) {
		super(n, PositiveInteger.class);
	}

	@Override
	public RuleResult<PositiveInteger> newRuleResultWithParsableValue(
			final String value) {
		return super.newRuleResult(PositiveInteger.newInstance(value));
	}

}
