package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;

public final class Rules {
	
	public static Rules newInstanceFrom(final Configuration configuration) {
		return Rules.of(configuration.getSettings().getValues(
				GeneralSettingSpecConstants.RULE));
	}
	
	public static Rules of(final List<Rule> rls) {
		return new Rules(rls);
	}
	
	public static Rules of(final Rule... rls) {
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
