package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public final class RuleArgSpec<V> {

	private final String string;
	private final Class<V> valueType;
	
	public RuleArgSpec(final String s, final Class<V> valType) {
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
		RuleArgSpec<?> other = (RuleArgSpec<?>) obj;
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

	public final RuleArg<V> newRuleArg(final V value) {
		return new RuleArg<V>(this, this.valueType.cast(value));
	}

	@Override
	public final String toString() {
		return this.string;
	}
	
}
