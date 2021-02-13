package jargyle.net.socks.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Properties {

	public static Properties newInstance(final List<Property> properties) {
		Map<PropertySpec, Property> props = 
				new HashMap<PropertySpec, Property>();
		for (Property property : properties) {
			props.put(property.getPropertySpec(), property);
		}
		for (PropertySpec propertySpec : PropertySpec.values()) {
			String property = System.getProperty(propertySpec.toString());
			if (property != null) {
				props.put(propertySpec, propertySpec.newProperty(property));
			}
		}
		return new Properties(props);
	}
	
	public static Properties newInstance(final Property... properties) {
		return newInstance(Arrays.asList(properties));
	}
	
	private final Map<PropertySpec, Property> properties;
	
	private Properties(final Map<PropertySpec, Property> props) {
		this.properties = new HashMap<PropertySpec, Property>(props);
	}
	
	private Properties(final Properties other) {
		this.properties = new HashMap<PropertySpec, Property>(other.properties);
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
	
	public <T> T getValue(
			final PropertySpec propertySpec, final Class<T> type) {
		T value = null;
		Property property = this.properties.get(propertySpec);
		if (property != null) {
			@SuppressWarnings("unchecked")
			T val = (T) property.getValue();
			value = val;
		}
		if (value == null) {
			@SuppressWarnings("unchecked")
			T val = (T) propertySpec.getDefaultProperty().getValue();
			value = val;
		}
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.properties == null) ? 0 : this.properties.hashCode());
		return result;
	}

	public Map<PropertySpec, Property> toMap() {
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
