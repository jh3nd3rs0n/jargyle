package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.List;

public final class ClientFirewallRules 
	extends FirewallRules<ClientFirewallRule> {

	private static final ClientFirewallRules DEFAULT_INSTANCE = 
			new ClientFirewallRules(Arrays.asList(
					ClientFirewallRule.getDefault()));
	
	public static ClientFirewallRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static ClientFirewallRules newInstance(
			final ClientFirewallRule... clientFirewallRules) {
		return newInstance(Arrays.asList(clientFirewallRules));
	}
	
	public static ClientFirewallRules newInstance(
			final List<ClientFirewallRule> clientFirewallRules) {
		return new ClientFirewallRules(clientFirewallRules);
	}
	
	public static ClientFirewallRules newInstance(final String s) {
		return newInstance(ClientFirewallRule.newInstances(s));
	}
	
	private ClientFirewallRules(
			final List<ClientFirewallRule> clientFirewallRules) {
		super(clientFirewallRules);
	}
	
}
