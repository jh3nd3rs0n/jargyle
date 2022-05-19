package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class StringRuleResultSpec extends RuleResultSpec<String> {

	public StringRuleResultSpec(final String s) {
		super(s, String.class);
	}

	@Override
	public RuleResult<String> newRuleResultOfParsableValue(final String value) {
		return super.newRuleResult(value);
	}

}
