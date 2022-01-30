package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rules;

public abstract class RoutingRules<R extends RoutingRule> extends Rules<R> {
	
	protected RoutingRules(final List<? extends R> routingRls) {
		super(routingRls);
	}
	
}
