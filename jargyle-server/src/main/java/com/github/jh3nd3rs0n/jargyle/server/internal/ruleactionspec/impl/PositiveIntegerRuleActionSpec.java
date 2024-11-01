package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class PositiveIntegerRuleActionSpec
	extends RuleActionSpec<PositiveInteger> {

	public PositiveIntegerRuleActionSpec(final String n) {
		super(n, PositiveInteger.class);
	}

	@Override
	protected PositiveInteger parse(final String value) {
		return PositiveInteger.valueOf(value);
	}

}
