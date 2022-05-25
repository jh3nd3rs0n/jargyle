package com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleConditionEvaluator;

public final class ValueEqualsValueRuleConditionEvaluator<V> 
	extends RuleConditionEvaluator<V, V> {

	public ValueEqualsValueRuleConditionEvaluator() {	}

	@Override
	public boolean evaluate(final V value1, final V value2) {
		return value1.equals(value2);
	}

}
