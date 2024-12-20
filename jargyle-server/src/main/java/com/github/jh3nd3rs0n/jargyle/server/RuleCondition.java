package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;

@NameValuePairValueTypeDoc(
		description = "",
		name = "Rule Condition",
		nameValuePairValueSpecs = {
				GeneralRuleConditionSpecConstants.class,
				Socks5RuleConditionSpecConstants.class
		},
		syntax = "NAME=VALUE",
		syntaxName = "RULE_CONDITION"
)
public final class RuleCondition<C, A> {

	public static RuleCondition<Object, Object> newInstanceFrom(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"rule condition must be in the following format: NAME=VALUE");
		}
		String name = sElements[0];
		String value = sElements[1];
		return newInstanceWithParsedValue(name, value);
	}
	
	public static <C, A> RuleCondition<C, A> newInstance(
			final String name, final C value) {
		RuleConditionSpec<Object, Object> ruleConditionSpec = 
				RuleConditionSpecConstants.valueOfName(name);
		@SuppressWarnings("unchecked")
		RuleCondition<C, A> ruleCondition =
				(RuleCondition<C, A>) ruleConditionSpec.newRuleCondition(
						value);
		return ruleCondition;
	}
	
	public static RuleCondition<Object, Object> newInstanceWithParsedValue(
			final String name, final String value) {
		RuleConditionSpec<Object, Object> ruleConditionSpec = 
				RuleConditionSpecConstants.valueOfName(name);
		return ruleConditionSpec.newRuleConditionWithParsedValue(value);
	}
	
	private final String name;
	private final RuleArgSpec<A> ruleArgSpec;
	private final RuleConditionEvaluator<C, A> ruleConditionEvaluator;
	private final RuleConditionSpec<C, A> ruleConditionSpec;
	private final C value;
			
	RuleCondition(
			final RuleConditionSpec<C, A> spec,
			final C val,
			final RuleConditionEvaluator<C, A> evaluator,
			final RuleArgSpec<A> rlArgSpec) {
		C v = spec.getValueType().cast(val);
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
	
	public RuleArgSpec<A> getRuleArgSpec() {
		return this.ruleArgSpec;
	}
	
	public RuleConditionEvaluator<C, A> getRuleConditionEvaluator() {
		return this.ruleConditionEvaluator;
	}
	
	public RuleConditionSpec<C, A> getRuleConditionSpec() {
		return this.ruleConditionSpec;
	}

	public C getValue() {
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
