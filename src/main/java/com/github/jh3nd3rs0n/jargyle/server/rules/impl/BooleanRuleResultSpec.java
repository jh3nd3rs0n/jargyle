package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class BooleanRuleResultSpec extends RuleResultSpec<Boolean> {

	public BooleanRuleResultSpec(final String s) {
		super(s, Boolean.class);
	}

	@Override
	public RuleResult<Boolean> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(Boolean.valueOf(value));
	}

}
