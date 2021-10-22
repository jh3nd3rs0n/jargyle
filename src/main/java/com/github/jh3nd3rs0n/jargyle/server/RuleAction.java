package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum RuleAction {

	ALLOW("allow"),
	
	BLOCK("block");

	public static RuleAction valueOfString(final String s) {
		for (RuleAction value : RuleAction.values()) {
			if (value.toString().equals(s)) {
				return value;
			}
		}
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
				"expected rule action must be one of the following values: %s. "
				+ "actual value is %s",
				sb.toString(),
				s));
	}

	private final String string;
	
	private RuleAction(final String str) {
		this.string = str;
	}
	
	@Override
	public String toString() {
		return this.string;
	}

}
