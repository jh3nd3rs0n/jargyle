package com.github.jh3nd3rs0n.jargyle.server.internal.ruleconditionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleArgSpec;
import com.github.jh3nd3rs0n.jargyle.server.RuleConditionSpec;

public final class StringEqualsStringRuleConditionSpec 
	extends RuleConditionSpec<String, String> {

	public StringEqualsStringRuleConditionSpec(
			final String n, final RuleArgSpec<String> rlArgSpec) {
		super(
				n, 
				String.class, 
				new ValueEqualsValueRuleConditionEvaluator<String>(), 
				rlArgSpec);
	}

	@Override
	protected String parse(final String value) {
		return value;
	}

}
