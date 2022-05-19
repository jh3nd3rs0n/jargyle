package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Rules {
	
	public static Rules newInstance(final Configuration configuration) {
		return Rules.newInstance(configuration.getSettings().getValues(
				GeneralSettingSpecConstants.RULE));
	}
	
	public static Rules newInstance(final List<Rule> rls) {
		return new Rules(rls);
	}
	
	public static Rules newInstance(final Rule... rls) {
		return new Rules(Arrays.asList(rls));
	}
	
	private final List<Rule> rules;

	private Rules(final List<Rule> rls) {
		this.rules = new ArrayList<Rule>(rls);
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

	public Rule firstAppliesTo(final RuleContext context) {
		for (Rule rule : this.rules) {
			if (rule.appliesTo(context)) {
				return rule;
			}
		}
		return null;
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
		return this.rules.stream().map(Rule::toString).collect(Collectors.joining(" "));
	}
}
