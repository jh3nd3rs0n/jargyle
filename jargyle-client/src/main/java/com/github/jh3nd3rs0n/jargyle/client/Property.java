package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;

@NameValuePairValueTypeDoc(
		description = "",
		name = "Property",
		nameValuePairValueSpecs = {
				SocksServerUriPropertySpecConstants.class,
				GeneralPropertySpecConstants.class,
				DtlsPropertySpecConstants.class,
				Socks5PropertySpecConstants.class,
				SslPropertySpecConstants.class
		},
		syntax = "NAME=VALUE",
		syntaxName = "PROPERTY"
)
public final class Property<V> {
	
	public static <V> Property<V> newInstance(
			final String name, final V value) {
		PropertySpec<Object> propertySpec = PropertySpecConstants.valueOfName(
				name);
		@SuppressWarnings("unchecked")
		Property<V> property = (Property<V>) propertySpec.newProperty(value);
		return property;
	}
	
	public static Property<Object> newInstanceWithParsedValue(
			final String name, final String value) {
		PropertySpec<Object> propertySpec = PropertySpecConstants.valueOfName(
				name);
		return propertySpec.newPropertyWithParsedValue(value);
	}
	
	private final String name;
	private final PropertySpec<V> propertySpec;
	private final V value;
	
	Property(final PropertySpec<V> spec, final V val) {
		V v = spec.getValueType().cast(val);
		this.name = spec.getName();
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
