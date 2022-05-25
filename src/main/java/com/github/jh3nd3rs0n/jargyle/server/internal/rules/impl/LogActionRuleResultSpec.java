package com.github.jh3nd3rs0n.jargyle.server.internal.rules.impl;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class LogActionRuleResultSpec extends RuleResultSpec<LogAction> {

	public LogActionRuleResultSpec(final String s) {
		super(s, LogAction.class);
	}

	@Override
	public RuleResult<LogAction> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(LogAction.valueOfString(value));
	}

}
