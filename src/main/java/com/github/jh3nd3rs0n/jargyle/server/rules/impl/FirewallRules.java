package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Rules;

public abstract class FirewallRules<R extends FirewallRule> extends Rules<R> {
	
	protected FirewallRules(final List<? extends R> firewallRls) {
		super(firewallRls);
	}
	
	public final R anyAppliesBasedOn(final Rule.Context context) {
		R rule = super.anyAppliesBasedOn(context);
		if (rule == null) {
			throw new FirewallRuleNotFoundException(context);
		}
		return rule;
	}
	
}
