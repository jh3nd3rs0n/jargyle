package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum Action {

	ALLOW("allow"),
	
	BLOCK("block");

	public static Action valueOfString(final String s) {
		for (Action value : Action.values()) {
			if (value.toString().equals(s)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<Action> list = Arrays.asList(Action.values());
		for (Iterator<Action> iterator = list.iterator();
				iterator.hasNext();) {
			Action value = iterator.next();
			sb.append(value);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected action must be one of the following values: %s. "
				+ "actual value is %s",
				sb.toString(),
				s));
	}

	private final String string;
	
	private Action(final String str) {
		this.string = str;
	}
	
	@Override
	public String toString() {
		return this.string;
	}

}
