package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Properties {

	public static Properties of(
			final List<Property<? extends Object>> properties) {
		return new Properties(properties);
	}
	
	@SafeVarargs
	public static Properties of(
			final Property<? extends Object>... properties) {
		return of(Arrays.asList(properties));
	}
	
	private final Map<PropertySpec<Object>, Property<Object>> properties;
	
	private Properties(final List<Property<? extends Object>> props) {
		Map<PropertySpec<Object>, Property<Object>> map = 
				new LinkedHashMap<PropertySpec<Object>, Property<Object>>();
		for (Property<? extends Object> prop : props) {
			@SuppressWarnings("unchecked")
			PropertySpec<Object> propSpec = 
					(PropertySpec<Object>) prop.getPropertySpec();
			if (map.containsKey(propSpec)) {
				map.remove(propSpec);
			}
			@SuppressWarnings("unchecked")
			Property<Object> prp = (Property<Object>) prop;
			map.put(propSpec, prp);
		}
		this.properties = map;
	}
	
	private Properties(final Properties other) {
		this.properties = 
				new LinkedHashMap<PropertySpec<Object>, Property<Object>>(
						other.properties);
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
		Properties other = (Properties) obj;
		if (this.properties == null) { 
			if (other.properties != null) {
				return false;
			}
		} else if (!this.properties.equals(other.properties)) {
			return false;
		}
		return true;
	}
	
	public <V> V getValue(final PropertySpec<V> propertySpec) {
		V value = null;
		Property<Object> property = this.properties.get(propertySpec);
		if (property != null) {
			value = propertySpec.getValueType().cast(property.getValue());
		}
		if (value == null) {
			Property<V> defaultProperty = propertySpec.getDefaultProperty();
			value = defaultProperty.getValue();
		}
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.properties == null) ? 
				0 : this.properties.hashCode());
		return result;
	}

	public Map<PropertySpec<Object>, Property<Object>> toMap() {
		return Collections.unmodifiableMap(this.properties);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [properties=")
			.append(this.properties)
			.append("]");
		return builder.toString();
	}
	
}
