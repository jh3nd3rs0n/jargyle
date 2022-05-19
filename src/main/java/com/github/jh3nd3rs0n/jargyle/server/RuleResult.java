package com.github.jh3nd3rs0n.jargyle.server;

public final class RuleResult<V> {
	
	public static RuleResult<Object> newInstance(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"rule result must be in the following format: NAME=VALUE");
		}
		String name = sElements[0];
		String value = sElements[1];
		return newInstanceOfParsableValue(name, value);		
	}
	
	public static <V> RuleResult<V> newInstance(
			final String name, final V value) {
		@SuppressWarnings("unchecked")
		RuleResult<V> ruleResult = 
				(RuleResult<V>) RuleResultSpecConstants.valueOf(
						name).newRuleResult(value);
		return ruleResult;
	}
	
	public static RuleResult<Object> newInstanceOfParsableValue(
			final String name, final String value) {
		return RuleResultSpecConstants.valueOf(
				name).newRuleResultOfParsableValue(value);
	}
	
	private final RuleResultSpec<V> ruleResultSpec;
	private final V value;
	
	RuleResult(final RuleResultSpec<V> spec, final V val) {
		this.ruleResultSpec = spec;
		this.value = val;
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
		RuleResult<?> other = (RuleResult<?>) obj;
		if (this.ruleResultSpec == null) {
			if (other.ruleResultSpec != null) {
				return false;
			}
		} else if (!this.ruleResultSpec.equals(other.ruleResultSpec)) {
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
	
	public RuleResultSpec<V> getRuleResultSpec() {
		return this.ruleResultSpec;
	}
	
	public V getValue() {
		return this.ruleResultSpec.getValueType().cast(this.value);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.ruleResultSpec == null) ? 
				0 : this.ruleResultSpec.hashCode());
		result = prime * result + ((this.value == null) ? 
				0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", this.ruleResultSpec, this.value);
	}

}
