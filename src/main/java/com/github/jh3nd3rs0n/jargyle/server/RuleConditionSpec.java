package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public abstract class RuleConditionSpec<V1, V2> {

	private final RuleArgSpec<V2> ruleArgSpec;
	private final RuleConditionEvaluator<V1, V2> ruleConditionEvaluator;
	private final String string;
	private final Class<V1> valueType;
		
	public RuleConditionSpec(
			final String s, 
			final Class<V1> valType, 
			final RuleConditionEvaluator<V1, V2> evaluator,
			final RuleArgSpec<V2> rlArgSpec) {
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		Objects.requireNonNull(evaluator);
		Objects.requireNonNull(rlArgSpec);
		this.ruleArgSpec = rlArgSpec;		
		this.ruleConditionEvaluator = evaluator;
		this.string = s;
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
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
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
		result = prime * result + ((this.string == null) ? 
				0 : this.string.hashCode());
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
		return this.string;
	}
	
}
