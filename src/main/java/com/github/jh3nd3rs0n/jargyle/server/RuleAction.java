package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

public enum RuleAction {

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

	public static RuleAction valueOfString(final String s) {
		RuleAction ruleAction = null;
		try {
			ruleAction = RuleAction.valueOf(s);
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<RuleAction> list = Arrays.asList(RuleAction.values());
			for (Iterator<RuleAction> iterator = list.iterator();
					iterator.hasNext();) {
				RuleAction value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(String.format(
					"expected rule action must be one of the following values: "
					+ "%s. actual value is %s",
					sb.toString(),
					s));			
		}
		return ruleAction;
	}

}
