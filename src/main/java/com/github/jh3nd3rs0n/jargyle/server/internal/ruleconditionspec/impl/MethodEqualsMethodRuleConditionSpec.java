package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

public final class MethodEqualsMethodRuleConditionSpec 
	extends RuleConditionSpec<Method, Method> {

	public MethodEqualsMethodRuleConditionSpec(
			final String s, final RuleArgSpec<Method> rlArgSpec) {
		super(
				s, 
				Method.class, 
				new ValueEqualsValueRuleConditionEvaluator<Method>(), 
				rlArgSpec);
	}

	@Override
	public RuleCondition<Method, Method> newRuleConditionOfParsableValue(
			final String value) {
		return super.newRuleCondition(Method.valueOfString(value));
	}

}
