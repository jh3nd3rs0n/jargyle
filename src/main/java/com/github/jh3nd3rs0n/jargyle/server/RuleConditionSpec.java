package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public abstract class RuleConditionSpec<V1, V2> {

	private final String name;
	private final RuleArgSpec<V2> ruleArgSpec;
	private final RuleConditionEvaluator<V1, V2> ruleConditionEvaluator;
	private final Class<V1> valueType;
		
	public RuleConditionSpec(
			final String n, 
			final Class<V1> valType, 
			final RuleConditionEvaluator<V1, V2> evaluator,
			final RuleArgSpec<V2> rlArgSpec) {
		Objects.requireNonNull(n);
		Objects.requireNonNull(valType);
		Objects.requireNonNull(evaluator);
		Objects.requireNonNull(rlArgSpec);
		this.name = n;
		this.ruleArgSpec = rlArgSpec;		
		this.ruleConditionEvaluator = evaluator;
		this.valueType = valType;
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
		RuleConditionSpec<?, ?> other = (RuleConditionSpec<?, ?>) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final RuleArgSpec<V2> getRuleArgSpec() {
		return this.ruleArgSpec;
	}
	
	public final RuleConditionEvaluator<V1, V2> getRuleConditionEvaluator() {
		return this.ruleConditionEvaluator;
	}
	
	public final Class<V1> getValueType() {
		return this.valueType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		return result;
	}
	
	public RuleCondition<V1, V2> newRuleCondition(final V1 value) {
		return new RuleCondition<V1, V2>(
				this, 
				this.valueType.cast(value), 
				this.ruleConditionEvaluator, 
				this.ruleArgSpec);
	}

	public abstract RuleCondition<V1, V2> newRuleConditionOfParsableValue(
			final String value);
	
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [name=")
			.append(this.name)
			.append("]");
		return builder.toString();
	}
	
}
