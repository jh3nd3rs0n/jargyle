package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Rules {

	private static final Rules DEFAULT_INSTANCE = new Rules(
			Arrays.asList(Rule.getDefault()));
	
	public static Rules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Rules newInstance(final List<Rule> rls) {
		return new Rules(rls);
	}
	
	public static Rules newInstance(final Rule... rls) {
		return newInstance(Arrays.asList(rls));
	}
	
	public static Rules newInstance(final String s) {
		return newInstance(Rule.newInstances(s));
	}
	
	private final List<Rule> rules;
	
	private Rules(final List<Rule> rls) {
		this.rules = new ArrayList<Rule>(rls);
	}
	
	public Rule anyAppliesTo(
			final String sourceAddress, final String destinationAddress) {
		for (Rule rule : this.rules) {
			if (rule.appliesTo(sourceAddress, destinationAddress)) {
				return rule;
			}
		}
		throw new IllegalArgumentException(String.format(
				"source address %s to destination address %s does not apply to "
				+ "any rule",
				sourceAddress,
				destinationAddress));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Rules other = (Rules) obj;
		if (this.rules == null) {
			if (other.rules != null) {
				return false;
			}
		} else if (!this.rules.equals(other.rules)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.rules == null) ? 
				0 : this.rules.hashCode());
		return result;
	}

	public List<Rule> toList() {
		return Collections.unmodifiableList(this.rules);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Rule> iterator = this.rules.iterator(); 
				iterator.hasNext();) {
			Rule rule = iterator.next();
			builder.append(rule);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
	
}
