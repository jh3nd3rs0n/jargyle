package com.github.jh3nd3rs0n.jargyle.server;

public final class Rule {

	public static Rule newInstance(
			final Action action, final ConditionPredicate conditionPredicate) {
		return new Rule(action, conditionPredicate);
	}
	
	public static Rule newInstance(
			final Action action, 
			final ConditionPredicate conditionPredicate, 
			final String doc) {
		return new Rule(action, conditionPredicate, doc);
	}
	
	public static Rule newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"rule must be in the following format: "
					+ "ACTION:CONDITION_PREDICATE");
		}
		Action action = Action.valueOfString(sElements[0]);
		ConditionPredicate conditionPredicate = ConditionPredicate.newInstance(
				sElements[1]);
		return newInstance(action, conditionPredicate);
	}
	
	private final Action action;
	private final ConditionPredicate conditionPredicate;
	private final String doc;
	
	private Rule(final Action actn, final ConditionPredicate cond) {
		this(actn, cond, null);
	}
	
	private Rule(
			final Action actn, final ConditionPredicate cond, final String d) {
		this.action = actn;
		this.conditionPredicate = cond;
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
		if (this.action != other.action) {
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

	public Action getAction() {
		return this.action;
	}

	public ConditionPredicate getConditionPredicate() {
		return this.conditionPredicate;
	}

	public String getDoc() {
		return this.doc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.action == null) ? 
				0 : this.action.hashCode());
		result = prime * result + ((this.conditionPredicate == null) ? 
				0 : conditionPredicate.hashCode());
		return result;
	}
	

	@Override
	public String toString() {
		return this.action.toString().concat(":").concat(
				this.conditionPredicate.toString());
	}
	

}
