package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

public abstract class PropertySpec<V> {

	private Property<V> defaultProperty;
	private final V defaultValue;
	private final String name;
	private final Class<V> valueType;
	
	public PropertySpec(
			final String n, final Class<V> valType, final V defaultVal) {
		Objects.requireNonNull(n);
		Objects.requireNonNull(valType);
		this.defaultValue = valType.cast(defaultVal);
		this.name = n;
		this.valueType = valType;
		this.defaultProperty = null;
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
		PropertySpec<?> other = (PropertySpec<?>) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	public final Property<V> getDefaultProperty() {
		if (this.defaultProperty == null) {
			this.defaultProperty = new Property<V>(this, this.valueType.cast(
					this.defaultValue));
		}
		return this.defaultProperty;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final Class<V> getValueType() {
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

	public Property<V> newProperty(final V value) {
		return new Property<V>(this, this.valueType.cast(value));
	}

	public abstract Property<V> newPropertyWithParsedValue(final String value);
	
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
