package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;

@NameValuePairValueTypeDoc(
		description = "",
		name = "Rule Result",
		nameValuePairValueSpecs = {
				GeneralRuleResultSpecConstants.class,
				Socks5RuleResultSpecConstants.class
		},
		syntax = "NAME=VALUE",
		syntaxName = "RULE_RESULT"
)
public final class RuleResult<V> {
	
	public static RuleResult<Object> newInstanceFrom(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"rule result must be in the following format: NAME=VALUE");
		}
		String name = sElements[0];
		String value = sElements[1];
		return newInstanceWithParsedValue(name, value);
	}
	
	public static <V> RuleResult<V> newInstance(
			final String name, final V value) {
		RuleResultSpec<Object> ruleResultSpec = 
				RuleResultSpecConstants.valueOfName(name);
		@SuppressWarnings("unchecked")
		RuleResult<V> ruleResult = (RuleResult<V>) ruleResultSpec.newRuleResult(
				value);
		return ruleResult;
	}
	
	public static RuleResult<Object> newInstanceWithParsedValue(
			final String name, final String value) {
		RuleResultSpec<Object> ruleResultSpec = 
				RuleResultSpecConstants.valueOfName(name);
		return ruleResultSpec.newRuleResultWithParsedValue(value);
	}
	
	private final String name;
	private final RuleResultSpec<V> ruleResultSpec;
	private final V value;
	
	RuleResult(final RuleResultSpec<V> spec, final V val) {
		V v = spec.getValueType().cast(val);
		this.name = spec.getName();
		this.ruleResultSpec = spec;
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
		RuleResult<?> other = (RuleResult<?>) obj;
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
