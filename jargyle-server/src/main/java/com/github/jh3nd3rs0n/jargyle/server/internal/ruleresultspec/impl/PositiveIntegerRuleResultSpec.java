package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.lang.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class PositiveIntegerRuleResultSpec 
	extends RuleResultSpec<PositiveInteger> {

	public PositiveIntegerRuleResultSpec(final String n) {
		super(n, PositiveInteger.class);
	}

	@Override
	public RuleResult<PositiveInteger> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(PositiveInteger.newInstance(value));
	}

}
