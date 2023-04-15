package com.github.jh3nd3rs0n.jargyle.server;

public final class RuleCondition<V1, V2> {

	public static RuleCondition<Object, Object> newInstance(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"rule condition must be in the following format: NAME=VALUE");
		}
		String name = sElements[0];
		String value = sElements[1];
		return newInstanceOfParsableValue(name, value);
	}
	
	public static <V1, V2> RuleCondition<V1, V2> newInstance(
			final String name, final V1 value) {
		@SuppressWarnings("unchecked")
		RuleCondition<V1, V2> ruleCondition = 
				(RuleCondition<V1, V2>) RuleConditionSpecConstants.valueOfName(
						name).newRuleCondition(value);
		return ruleCondition;
	}
	
	public static RuleCondition<Object, Object> newInstanceOfParsableValue(
			final String name, final String value) {
		return RuleConditionSpecConstants.valueOfName(
				name).newRuleConditionOfParsableValue(value);
	}
	
	private final String name;
	private final RuleArgSpec<V2> ruleArgSpec;	
	private final RuleConditionEvaluator<V1, V2> ruleConditionEvaluator;
	private final RuleConditionSpec<V1, V2> ruleConditionSpec;
	private final V1 value;
			
	RuleCondition(
			final RuleConditionSpec<V1, V2> spec, 
			final V1 val,
			final RuleConditionEvaluator<V1, V2> evaluator, 
			final RuleArgSpec<V2> rlArgSpec) {
		V1 v = spec.getValueType().cast(val);
		this.name = spec.getName();
		this.ruleArgSpec = rlArgSpec;		
		this.ruleConditionEvaluator = evaluator;
		this.ruleConditionSpec = spec;
		this.value = v;
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
		RuleCondition<?, ?> other = (RuleCondition<?, ?>) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
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
	
	public boolean evaluatesTrue(final RuleContext ruleContext) {
		if (!ruleContext.containsRuleArgSpecKey(this.ruleArgSpec)) {
			return false;
		}
		return this.ruleConditionEvaluator.evaluate(
				this.value, ruleContext.getRuleArgValue(this.ruleArgSpec));
	}

	public String getName() {
		return this.name;
	}
	
	public RuleArgSpec<V2> getRuleArgSpec() {
		return this.ruleArgSpec;
	}
	
	public RuleConditionEvaluator<V1, V2> getRuleConditionEvaluator() {
		return this.ruleConditionEvaluator;
	}
	
	public RuleConditionSpec<V1, V2> getRuleConditionSpec() {
		return this.ruleConditionSpec;
	}

	public V1 getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		result = prime * result + ((this.value == null) ? 
				0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", this.name, this.value);
	}
	
}
