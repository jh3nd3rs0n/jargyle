package com.github.jh3nd3rs0n.jargyle.server;

public abstract class RuleConditionEvaluator<C, A> {

	public abstract boolean evaluate(final C conditionValue, final A argValue);
	
}
