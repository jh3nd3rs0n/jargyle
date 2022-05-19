package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public abstract class RuleResultSpec<V> {

	private final String string;
	private final Class<V> valueType;
	
	public RuleResultSpec(final String s, final Class<V> valType) {
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
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
		RuleResultSpec<?> other = (RuleResultSpec<?>) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}
	
	public final Class<V> getValueType() {
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
	
	public RuleResult<V> newRuleResult(final V value) {
		return new RuleResult<V>(this, this.valueType.cast(value));
	}
	
	public abstract RuleResult<V> newRuleResultOfParsableValue(
			final String value);
	
	@Override
	public final String toString() {
		return this.string;
	}

}
