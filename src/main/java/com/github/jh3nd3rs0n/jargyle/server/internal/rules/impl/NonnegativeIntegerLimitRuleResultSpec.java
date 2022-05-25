package com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class NonnegativeIntegerLimitRuleResultSpec 
	extends RuleResultSpec<NonnegativeIntegerLimit> {

	public NonnegativeIntegerLimitRuleResultSpec(final String s) {
		super(s, NonnegativeIntegerLimit.class);
	}

	@Override
	public RuleResult<NonnegativeIntegerLimit> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(NonnegativeIntegerLimit.newInstance(value));
	}

}
