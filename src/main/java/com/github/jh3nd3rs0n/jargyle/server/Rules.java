package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Rules {

	private static final Rules EMPTY_INSTANCE = new Rules(
			Collections.emptyList());
	
	public static Rules getEmptyInstance() {
		return EMPTY_INSTANCE;
	}
	
	public static Rules newInstance(final List<Rule> rls) {
		return new Rules(rls);
	}
	
	public static Rules newInstance(final Rule... rls) {
		return newInstance(Arrays.asList(rls));
	}
	
	public static Rules newInstance(final String s) {
		List<Rule> rules = new ArrayList<Rule>();
		if (s.isEmpty()) {
			return new Rules(rules);
		}
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			rules.add(Rule.newInstance(sElement));
		}
		return new Rules(rules);
	}
	
	private final List<Rule> rules;
	
	private Rules(final List<Rule> rls) {
		this.rules = new ArrayList<Rule>(rls);
	}
	
	public Rule anyAppliesTo(final String str) {
		for (Rule rule : this.rules) {
			if (rule.appliesTo(str)) {
				return rule;
			}
		}
		return null;
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
			builder.append(rule.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
	
}
