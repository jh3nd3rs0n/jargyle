package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class PositiveIntegerRuleResultSpec 
	extends RuleResultSpec<PositiveInteger> {

	public PositiveIntegerRuleResultSpec(final String n) {
		super(n, PositiveInteger.class);
	}

	@Override
	protected PositiveInteger parse(final String value) {
		return PositiveInteger.valueOf(value);
	}

}
