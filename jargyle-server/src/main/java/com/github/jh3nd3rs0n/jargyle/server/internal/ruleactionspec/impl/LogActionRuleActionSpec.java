package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class LogActionRuleActionSpec extends RuleActionSpec<LogAction> {

	public LogActionRuleActionSpec(final String n) {
		super(n, LogAction.class);
	}

	@Override
	protected LogAction parse(final String value) {
		return LogAction.valueOfString(value);
	}

}
