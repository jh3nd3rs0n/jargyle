package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;

public final class SelectionStrategyRuleResultSpec 
	extends RuleResultSpec<SelectionStrategy> {

	public SelectionStrategyRuleResultSpec(final String n) {
		super(n, SelectionStrategy.class);
	}

	@Override
	public RuleResult<SelectionStrategy> newRuleResultWithParsedValue(
			final String value) {
		return super.newRuleResult(SelectionStrategy.newInstanceFrom(value));
	}

}
