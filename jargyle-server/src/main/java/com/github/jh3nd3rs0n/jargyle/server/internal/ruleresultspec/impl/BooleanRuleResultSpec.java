package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class BooleanRuleResultSpec extends RuleResultSpec<Boolean> {

	public BooleanRuleResultSpec(final String n) {
		super(n, Boolean.class);
	}

	@Override
	public RuleResult<Boolean> newRuleResultWithParsableValue(
			final String value) {
		return super.newRuleResult(Boolean.valueOf(value));
	}

}
