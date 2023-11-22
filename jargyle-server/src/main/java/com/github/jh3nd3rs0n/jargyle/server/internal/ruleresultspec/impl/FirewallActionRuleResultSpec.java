package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class FirewallActionRuleResultSpec 
	extends RuleResultSpec<FirewallAction> {

	public FirewallActionRuleResultSpec(final String n) {
		super(n, FirewallAction.class);
	}

	@Override
	public RuleResult<FirewallAction> newRuleResultWithParsableValue(
			final String value) {
		return super.newRuleResult(FirewallAction.valueOfString(value));
	}

}
