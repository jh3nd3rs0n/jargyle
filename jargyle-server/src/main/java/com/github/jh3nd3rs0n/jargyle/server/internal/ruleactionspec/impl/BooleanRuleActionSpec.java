package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class BooleanRuleActionSpec extends RuleActionSpec<Boolean> {

	public BooleanRuleActionSpec(final String n) {
		super(n, Boolean.class);
	}

	@Override
	protected Boolean parse(final String value) {
		return Boolean.valueOf(value);
	}

}
