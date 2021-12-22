package com.github.jh3nd3rs0n.jargyle.server;

public final class FirewallRuleActionDenyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final FirewallRule firewallRule;
	private final FirewallRule.Context context;
	
	public FirewallRuleActionDenyException(
			final FirewallRule firewallRl, final FirewallRule.Context cntxt) {
		super(String.format(
				"firewall rule action is DENY from the following firewall rule "
				+ "and its context: %s, %s", 
				firewallRl, 
				cntxt));
		this.firewallRule = firewallRl;
		this.context = cntxt;
	}
	
	public FirewallRule.Context getContext() {
		return this.context;
	}
	
	public FirewallRule getRule() {
		return this.firewallRule;
	}

}
