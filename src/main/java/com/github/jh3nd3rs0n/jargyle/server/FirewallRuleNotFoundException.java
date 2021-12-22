package com.github.jh3nd3rs0n.jargyle.server;

public final class FirewallRuleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final FirewallRule.Context context;
	
	FirewallRuleNotFoundException(final FirewallRule.Context cntxt) {
		super(String.format(
				"rule not found for the following context: %s", 
				cntxt));
		this.context = cntxt;
	}
	
	public FirewallRule.Context getContext() {
		return this.context;
	}

}
