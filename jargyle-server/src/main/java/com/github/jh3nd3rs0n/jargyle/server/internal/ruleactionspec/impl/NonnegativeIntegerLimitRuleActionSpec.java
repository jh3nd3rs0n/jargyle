package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.NonNegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class NonnegativeIntegerLimitRuleActionSpec
	extends RuleActionSpec<NonNegativeIntegerLimit> {

	public NonnegativeIntegerLimitRuleActionSpec(final String n) {
		super(n, NonNegativeIntegerLimit.class);
	}

	@Override
	protected NonNegativeIntegerLimit parse(final String value) {
		return NonNegativeIntegerLimit.newInstanceFrom(value);
	}

}
