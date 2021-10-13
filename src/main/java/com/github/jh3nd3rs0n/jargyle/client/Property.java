package com.github.jh3nd3rs0n.jargyle.client;

public final class Property<V> {
	
	public static <V> Property<V> newInstance(
			final String name, final V value) {
		@SuppressWarnings("unchecked")
		Property<V> property = (Property<V>) PropertySpecConstants.valueOf(
				name).newProperty(value);
		return property;
	}
	
	public static Property<Object> newInstanceOfParsableValue(
			final String name, final String value) {
		return PropertySpecConstants.valueOf(name).newPropertyOfParsableValue(
				value);
	}
	
	private final PropertySpec<V> propertySpec;
	private final V value;
	
	Property(final PropertySpec<V> spec, final V val) {
		V v = spec.getValueType().cast(val);
		this.propertySpec = spec;
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
		Property<?> other = (Property<?>) obj;
		if (this.propertySpec != other.propertySpec) {
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
	
	public PropertySpec<V> getPropertySpec() {
		return this.propertySpec;
	}
	
	public V getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.propertySpec == null) ? 
				0 : this.propertySpec.hashCode());
		result = prime * result + ((this.value == null) ? 
				0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", this.propertySpec, this.value);
	}
}
