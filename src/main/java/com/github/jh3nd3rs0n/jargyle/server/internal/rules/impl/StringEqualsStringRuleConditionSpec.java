package com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;

public final class StringEqualsStringRuleConditionSpec 
	extends RuleConditionSpec<String, String> {

	public StringEqualsStringRuleConditionSpec(
			final String s, final RuleArgSpec<String> rlArgSpec) {
		super(
				s, 
				String.class, 
				new ValueEqualsValueRuleConditionEvaluator<String>(), 
				rlArgSpec);
	}

	@Override
	public RuleCondition<String, String> newRuleConditionOfParsableValue(
			final String value) {
		return super.newRuleCondition(value);
	}

}
