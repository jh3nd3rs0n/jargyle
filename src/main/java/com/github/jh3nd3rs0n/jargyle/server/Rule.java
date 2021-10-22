package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public final class Rule {

	public static Rule newInstance(
			final RuleAction ruleAction, 
			final ConditionPredicate conditionPredicate) {
		return new Rule(ruleAction, conditionPredicate);
	}
	
	public static Rule newInstance(
			final RuleAction ruleAction, 
			final ConditionPredicate conditionPredicate, 
			final String doc) {
		return new Rule(ruleAction, conditionPredicate, doc);
	}
	
	public static Rule newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"rule must be in the following format: "
					+ "RULE_ACTION:CONDITION_PREDICATE");
		}
		RuleAction ruleAction = RuleAction.valueOfString(sElements[0]);
		ConditionPredicate conditionPredicate = ConditionPredicate.newInstance(
				sElements[1]);
		return newInstance(ruleAction, conditionPredicate);
	}
	
	private final RuleAction ruleAction;
	private final ConditionPredicate conditionPredicate;
	private final String doc;
	
	private Rule(
			final RuleAction rlAction, final ConditionPredicate condPredicate) {
		this(rlAction, condPredicate, null);
	}
	
	private Rule(
			final RuleAction rlAction, 
			final ConditionPredicate condPredicate, 
			final String d) {
		this.ruleAction = Objects.requireNonNull(
				rlAction, "rule action must not be null");
		this.conditionPredicate = Objects.requireNonNull(
				condPredicate, "condition predicate must not be null");
		this.doc = d;
	}

	public boolean appliesTo(final String arg) {
		return this.conditionPredicate.evaluate(arg);
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
		Rule other = (Rule) obj;
		if (this.ruleAction != other.ruleAction) {
			return false;
		}
		if (this.conditionPredicate == null) {
			if (other.conditionPredicate != null) {
				return false;
			}
		} else if (!this.conditionPredicate.equals(other.conditionPredicate)) {
			return false;
		}
		return true;
	}

	public ConditionPredicate getConditionPredicate() {
		return this.conditionPredicate;
	}

	public String getDoc() {
		return this.doc;
	}

	public RuleAction getRuleAction() {
		return this.ruleAction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.ruleAction == null) ? 
				0 : this.ruleAction.hashCode());
		result = prime * result + ((this.conditionPredicate == null) ? 
				0 : conditionPredicate.hashCode());
		return result;
	}
	

	@Override
	public String toString() {
		return this.ruleAction.toString().concat(":").concat(
				this.conditionPredicate.toString());
	}
	

}
