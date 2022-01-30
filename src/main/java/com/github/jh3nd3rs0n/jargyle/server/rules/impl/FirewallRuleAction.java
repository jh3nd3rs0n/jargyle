package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

public enum FirewallRuleAction {

	@HelpText(
			doc = "",
			usage = "ALLOW"
	)
	ALLOW,
	
	@HelpText(
			doc = "",
			usage = "DENY"
	)	
	DENY;

	public static FirewallRuleAction valueOfString(final String s) {
		FirewallRuleAction firewallRuleAction = null;
		try {
			firewallRuleAction = FirewallRuleAction.valueOf(s);
		} catch (IllegalArgumentException e) {
			String str = Arrays.stream(FirewallRuleAction.values())
					.map(FirewallRuleAction::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected firewall rule action must be one of the "
					+ "following values: %s. actual value is %s",
					str,
					s));			
		}
		return firewallRuleAction;
	}

}
