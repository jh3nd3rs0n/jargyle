package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;

public final class SelectionStrategyRuleResultSpec 
	extends RuleResultSpec<SelectionStrategy> {

	public SelectionStrategyRuleResultSpec(final String n) {
		super(n, SelectionStrategy.class);
	}

	@Override
	protected SelectionStrategy parse(final String value) {
		return SelectionStrategy.newInstanceFrom(value);
	}

}
