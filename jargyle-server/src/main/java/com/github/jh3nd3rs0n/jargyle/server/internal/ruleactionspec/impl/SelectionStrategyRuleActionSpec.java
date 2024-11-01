package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;

public final class SelectionStrategyRuleActionSpec
	extends RuleActionSpec<SelectionStrategy> {

	public SelectionStrategyRuleActionSpec(final String n) {
		super(n, SelectionStrategy.class);
	}

	@Override
	protected SelectionStrategy parse(final String value) {
		return SelectionStrategy.newInstanceFrom(value);
	}

}
