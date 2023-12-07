package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

@EnumValueTypeDoc(
		description = "",
		name = "Firewall Action",
		syntax = "ALLOW|DENY",
		syntaxName = "FIREWALL_ACTION"
)
public enum FirewallAction {

	@EnumValueDoc(description = "", value = "ALLOW")
	ALLOW,
	
	@EnumValueDoc(description = "", value = "DENY")	
	DENY;

	public static FirewallAction valueOfString(final String s) {
		FirewallAction firewallAction = null;
		try {
			firewallAction = FirewallAction.valueOf(s);
		} catch (IllegalArgumentException e) {
			String str = Arrays.stream(FirewallAction.values())
					.map(FirewallAction::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected firewall rule action must be one of the "
					+ "following values: %s. actual value is %s",
					str,
					s));			
		}
		return firewallAction;
	}

}
