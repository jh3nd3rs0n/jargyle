package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ClientRoutingRules extends RoutingRules<ClientRoutingRule> {

	private static ClientRoutingRules DEFAULT_INSTANCE = new ClientRoutingRules(
			Collections.emptyList());
	
	public static ClientRoutingRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static ClientRoutingRules newInstance(
			final List<ClientRoutingRule> clientRoutingRules) {
		return new ClientRoutingRules(clientRoutingRules);
	}
	
	public static ClientRoutingRules newInstance(
			final ClientRoutingRule... clientRoutingRules) {
		return newInstance(Arrays.asList(clientRoutingRules));
	}
	
	public static ClientRoutingRules newInstance(final String s) {
		return newInstance(ClientRoutingRule.newInstances(s));
	}

	private ClientRoutingRules(
			final List<ClientRoutingRule> clientRoutingRules) {
		super(clientRoutingRules);
	}

}
