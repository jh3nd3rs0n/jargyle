package com.github.jh3nd3rs0n.jargyle.server;

public abstract class RuleConditionEvaluator<V1, V2> {

	public abstract boolean evaluate(final V1 value1, final V2 value2);
	
}
