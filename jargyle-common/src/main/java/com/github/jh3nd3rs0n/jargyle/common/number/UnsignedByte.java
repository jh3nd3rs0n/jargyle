package com.github.jh3nd3rs0n.jargyle.common.number;

public final class UnsignedByte {

	public static final int MAX_INT_VALUE = 0xff;
	public static final int MIN_INT_VALUE = 0;
	
	public static UnsignedByte newInstance(final byte b) {
		return newInstance(b & MAX_INT_VALUE);
	}
	
	public static UnsignedByte newInstance(final int i) {
		if (i < MIN_INT_VALUE || i > MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"expected an integer between %s and %s (inclusive). "
					+ "actual value is %s", 
					MIN_INT_VALUE,
					MAX_INT_VALUE,
					i));
		}
		return new UnsignedByte(i);
	}
	
	public static UnsignedByte newInstance(final String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format(
					"expected an integer between %s and %s (inclusive). "
					+ "actual value is %s", 
					MIN_INT_VALUE,
					MAX_INT_VALUE,
					s),
					e);
		}
		return newInstance(i);
	}
	
	private final int intValue;
	
	private UnsignedByte(final int i) {
		this.intValue = i;
	}
	
	public byte byteValue() {
		return (byte) this.intValue;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		UnsignedByte other = (UnsignedByte) obj;
		if (this.intValue != other.intValue) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.intValue;
		return result;
	}

	public int intValue() {
		return this.intValue;
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.intValue);
	}	
}
