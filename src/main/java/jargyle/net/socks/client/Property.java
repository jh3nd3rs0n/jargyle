package jargyle.net.socks.client;

public final class Property {

	public static Property newInstance(
			final PropertySpec spec, final Object val) {
		return new Property(spec, val);
	}
	
	private final PropertySpec propertySpec;
	private final Object value;
	
	private Property(final PropertySpec spec, final Object val) {
		this.propertySpec = spec;
		this.value = val;
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
		Property other = (Property) obj;
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
	
	public PropertySpec getPropertySpec() {
		return this.propertySpec;
	}
	
	public Object getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.propertySpec == null) ? 0 : this.propertySpec.hashCode());
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", this.propertySpec, this.value);
	}
}
