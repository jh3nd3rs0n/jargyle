package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.NonNegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class NonnegativeIntegerLimitRuleResultSpec 
	extends RuleResultSpec<NonNegativeIntegerLimit> {

	public NonnegativeIntegerLimitRuleResultSpec(final String n) {
		super(n, NonNegativeIntegerLimit.class);
	}

	@Override
	protected NonNegativeIntegerLimit parse(final String value) {
		return NonNegativeIntegerLimit.newInstanceFrom(value);
	}

}
