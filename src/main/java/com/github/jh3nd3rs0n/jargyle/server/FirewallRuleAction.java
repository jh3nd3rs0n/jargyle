package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
			StringBuilder sb = new StringBuilder();
			List<FirewallRuleAction> list = Arrays.asList(
					FirewallRuleAction.values());
			for (Iterator<FirewallRuleAction> iterator = list.iterator();
					iterator.hasNext();) {
				FirewallRuleAction value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(String.format(
					"expected firewall rule action must be one of the "
					+ "following values: %s. actual value is %s",
					sb.toString(),
					s));			
		}
		return firewallRuleAction;
	}

}
