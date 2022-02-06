package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rules;

public abstract class FirewallRules<R extends FirewallRule> extends Rules<R> {
	
	public FirewallRules(final List<? extends R> firewallRls) {
		super(firewallRls);
	}
	
}