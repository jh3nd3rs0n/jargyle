package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;

@NameValuePairValueTypeDoc(
		description = "",
		name = "Rule Action",
		nameValuePairValueSpecs = {
				GeneralRuleActionSpecConstants.class,
				Socks5RuleActionSpecConstants.class
		},
		syntax = "NAME=VALUE",
		syntaxName = "RULE_ACTION"
)
public final class RuleAction<V> {
	
	public static RuleAction<Object> newInstanceFrom(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"rule action must be in the following format: NAME=VALUE");
		}
		String name = sElements[0];
		String value = sElements[1];
		return newInstanceWithParsedValue(name, value);
	}
	
	public static <V> RuleAction<V> newInstance(
			final String name, final V value) {
		RuleActionSpec<Object> ruleActionSpec = 
				RuleActionSpecConstants.valueOfName(name);
		@SuppressWarnings("unchecked")
        RuleAction<V> ruleAction = (RuleAction<V>) ruleActionSpec.newRuleAction(
				value);
		return ruleAction;
	}
	
	public static RuleAction<Object> newInstanceWithParsedValue(
			final String name, final String value) {
		RuleActionSpec<Object> ruleActionSpec = 
				RuleActionSpecConstants.valueOfName(name);
		return ruleActionSpec.newRuleActionWithParsedValue(value);
	}
	
	private final String name;
	private final RuleActionSpec<V> ruleActionSpec;
	private final V value;
	
	RuleAction(final RuleActionSpec<V> spec, final V val) {
		V v = spec.getValueType().cast(val);
		this.name = spec.getName();
		this.ruleActionSpec = spec;
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
		RuleAction<?> other = (RuleAction<?>) obj;
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
	
	public String getName() {
		return this.name;
	}
	
	public RuleActionSpec<V> getRuleActionSpec() {
		return this.ruleActionSpec;
	}
	
	public V getValue() {
		return this.ruleActionSpec.getValueType().cast(this.value);
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
