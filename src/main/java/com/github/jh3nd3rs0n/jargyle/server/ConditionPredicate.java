package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public final class ConditionPredicate {

	public static ConditionPredicate newInstance(
			final ConditionPredicateMethod conditionPredicateMethod, 
			final String value) {
		return new ConditionPredicate(conditionPredicateMethod, value);
	}
	
	public static ConditionPredicate newInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"condition predicate must be in the following format: "
					+ "CONDITION_PREDICATE_METHOD:VALUE");
		}
		ConditionPredicateMethod conditionPredicateMethod = 
				ConditionPredicateMethod.valueOfString(sElements[0]);
		String value = sElements[1];
		return ConditionPredicate.newInstance(conditionPredicateMethod, value);
	}
	
	private final ConditionPredicateMethod conditionPredicateMethod;
	private final String value;
	
	private ConditionPredicate(
			final ConditionPredicateMethod condPredicateMethod, 
			final String val) {
		this.conditionPredicateMethod = Objects.requireNonNull(
				condPredicateMethod, 
				"condition predicate method must not be null");
		this.value = Objects.requireNonNull(val, "value must not be null");
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
		ConditionPredicate other = (ConditionPredicate) obj;
		if (this.conditionPredicateMethod != other.conditionPredicateMethod) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}
	
	public boolean evaluate(final String arg) {
		return this.conditionPredicateMethod.evaluate(arg, this.value);
	}

	public ConditionPredicateMethod getConditionPredicateMethod() {
		return this.conditionPredicateMethod;
	}
	
	public String getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.conditionPredicateMethod == null) ? 
				0 : this.conditionPredicateMethod.hashCode());
		result = prime * result + ((this.value == null) ? 
				0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return this.conditionPredicateMethod.toString().concat(":").concat(
				this.value);
	}
	
}
