package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class StringRuleActionSpec extends RuleActionSpec<String> {

	public StringRuleActionSpec(final String n) {
		super(n, String.class);
	}

	@Override
	protected String parse(final String value) {
		return value;
	}

}
