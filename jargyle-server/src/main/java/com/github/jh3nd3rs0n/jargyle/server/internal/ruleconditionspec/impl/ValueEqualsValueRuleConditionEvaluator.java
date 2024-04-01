package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleConditionEvaluator;

public final class ValueEqualsValueRuleConditionEvaluator<V> 
	extends RuleConditionEvaluator<V, V> {

	public ValueEqualsValueRuleConditionEvaluator() {	}

	@Override
	public boolean evaluate(final V conditionValue, final V argValue) {
		return conditionValue.equals(argValue);
	}

}
