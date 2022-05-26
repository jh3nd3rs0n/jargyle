package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;

public final class SelectionStrategyRuleResultSpec 
	extends RuleResultSpec<SelectionStrategy> {

	public SelectionStrategyRuleResultSpec(final String s) {
		super(s, SelectionStrategy.class);
	}

	@Override
	public RuleResult<SelectionStrategy> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(
				SelectionStrategy.valueOf(value).newMutableInstance());
	}

}
