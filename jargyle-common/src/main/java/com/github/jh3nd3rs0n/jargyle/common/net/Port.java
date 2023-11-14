package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

public final class Port implements Comparable<Port> {

	public static final int MAX_INT_VALUE = UnsignedShort.MAX_INT_VALUE;
	public static final int MIN_INT_VALUE = UnsignedShort.MIN_INT_VALUE;
	
	public static Port newInstance(final byte[] b) {
		return new Port(UnsignedShort.newInstance(b));
	}
	
	public static Port newInstance(final int i) {
		return new Port(UnsignedShort.newInstance(i));
	}
	
	public static Port newInstance(final short s) {
		return new Port(UnsignedShort.newInstance(s));
	}
	
	public static Port newInstance(final String s) {
		return new Port(UnsignedShort.newInstance(s));
	}
	
	public static Port newInstance(final UnsignedShort s) {
		return new Port(s);
	}
	
	private final UnsignedShort unsignedShortValue;
	
	private Port(final UnsignedShort unsignedShortVal) {
		this.unsignedShortValue = unsignedShortVal;
	}
	
	@Override
	public int compareTo(final Port other) {
		return this.unsignedShortValue.intValue() - other.unsignedShortValue.intValue();
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
		Port other = (Port) obj;
		if (this.unsignedShortValue == null) {
			if (other.unsignedShortValue != null) {
				return false;
			}
		} else if (!this.unsignedShortValue.equals(other.unsignedShortValue)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.unsignedShortValue == null) ? 
				0 : this.unsignedShortValue.hashCode());
		return result;
	}

	public int intValue() {
		return this.unsignedShortValue.intValue();
	}
	
	public short shortValue() {
		return this.unsignedShortValue.shortValue();
	}
	
	public byte[] toByteArray() {
		return this.unsignedShortValue.toByteArray();
	}
	
	public String toString() {
		return this.unsignedShortValue.toString();
	}
	
	public UnsignedShort toUnsignedShort() {
		return this.unsignedShortValue;
	}
	
}
