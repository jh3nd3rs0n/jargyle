package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public abstract class RuleConditionSpec<C, A> {

	private final String name;
	private final RuleArgSpec<A> ruleArgSpec;
	private final RuleConditionEvaluator<C, A> ruleConditionEvaluator;
	private final Class<C> valueType;
		
	public RuleConditionSpec(
			final String n, 
			final Class<C> valType,
			final RuleConditionEvaluator<C, A> evaluator,
			final RuleArgSpec<A> rlArgSpec) {
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
	
	public final RuleArgSpec<A> getRuleArgSpec() {
		return this.ruleArgSpec;
	}
	
	public final RuleConditionEvaluator<C, A> getRuleConditionEvaluator() {
		return this.ruleConditionEvaluator;
	}
	
	public final Class<C> getValueType() {
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
	
	public final RuleCondition<C, A> newRuleCondition(final C value) {
		return new RuleCondition<C, A>(
				this, 
				this.valueType.cast(this.validate(value)),
				this.ruleConditionEvaluator, 
				this.ruleArgSpec);
	}

	public final RuleCondition<C, A> newRuleConditionWithParsedValue(
			final String value) {
		return this.newRuleCondition(this.parse(value));
	}

	protected abstract C parse(final String value);

	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [name=")
			.append(this.name)
			.append("]");
		return builder.toString();
	}

	protected C validate(final C value) {
		return value;
	}

}
