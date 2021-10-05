package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Objects;

public abstract class PropertySpec<V> {

	private Property<V> defaultProperty;
	private final V defaultValue;
	private final String string;
	private final Class<V> valueType;
	
	public PropertySpec(
			final Object permission, 
			final String s, 
			final Class<V> valType, 
			final V defaultVal) {
		Objects.requireNonNull(permission);
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		if (!NewPropertySpecPermission.INSTANCE.equals(permission)) {
			throw new IllegalArgumentException(
					"invalid new PropertySpec permission");
		}
		this.defaultValue = valType.cast(defaultVal);
		this.string = s;
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
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
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

	public Property<V> newProperty(final V value) {
		return new Property<V>(this, this.valueType.cast(value));
	}

	public abstract Property<V> newPropertyOfParsableValue(final String value);
	
	@Override
	public final String toString() {
		return this.string;
	}
	
}
