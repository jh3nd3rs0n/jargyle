package com.github.jh3nd3rs0n.jargyle.server.internal.ruleactionspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionSpec;

public final class FirewallActionRuleActionSpec
	extends RuleActionSpec<FirewallAction> {

	public FirewallActionRuleActionSpec(final String n) {
		super(n, FirewallAction.class);
	}

	@Override
	protected FirewallAction parse(final String value) {
		return FirewallAction.valueOfString(value);
	}

}
