package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class LogActionRuleResultSpec extends RuleResultSpec<LogAction> {

	public LogActionRuleResultSpec(final String n) {
		super(n, LogAction.class);
	}

	@Override
	protected LogAction parse(final String value) {
		return LogAction.valueOfString(value);
	}

}
