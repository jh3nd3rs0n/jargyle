package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class NonnegativeIntegerLimitRuleResultSpec 
	extends RuleResultSpec<NonnegativeIntegerLimit> {

	public NonnegativeIntegerLimitRuleResultSpec(final String n) {
		super(n, NonnegativeIntegerLimit.class);
	}

	@Override
	public RuleResult<NonnegativeIntegerLimit> newRuleResultWithParsableValue(
			final String value) {
		return super.newRuleResult(NonnegativeIntegerLimit.newInstance(value));
	}

}
