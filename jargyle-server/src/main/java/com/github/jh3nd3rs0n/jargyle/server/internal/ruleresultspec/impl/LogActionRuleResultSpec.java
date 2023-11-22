package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class LogActionRuleResultSpec extends RuleResultSpec<LogAction> {

	public LogActionRuleResultSpec(final String n) {
		super(n, LogAction.class);
	}

	@Override
	public RuleResult<LogAction> newRuleResultWithParsableValue(
			final String value) {
		return super.newRuleResult(LogAction.valueOfString(value));
	}

}
