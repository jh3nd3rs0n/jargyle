package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;

public abstract class RoutingRules<R extends RoutingRule> extends Rules<R> {
	
	protected RoutingRules(final List<? extends R> routingRls) {
		super(routingRls);
	}
	
}
