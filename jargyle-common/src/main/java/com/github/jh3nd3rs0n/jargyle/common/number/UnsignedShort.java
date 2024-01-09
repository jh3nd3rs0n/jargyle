package com.github.jh3nd3rs0n.jargyle.common.number;

import java.nio.ByteBuffer;

public final class UnsignedShort {

	public static final int BYTE_ARRAY_LENGTH = 2;
	
	public static final int MAX_INT_VALUE = 0xffff;
	public static final int MIN_INT_VALUE = 0x0000;
	
	public static UnsignedShort newInstanceOf(final byte[] b) {
		if (b.length != BYTE_ARRAY_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"expected a byte array of a length of %s. "
					+ "actual byte array length is %s", 
					BYTE_ARRAY_LENGTH,
					b.length));
		}
		ByteBuffer bb = ByteBuffer.wrap(new byte[] { b[0], b[1] });
		return newInstanceOf(bb.getShort() & MAX_INT_VALUE);
	}
	
	public static UnsignedShort newInstanceOf(final int i) {
		if (i < MIN_INT_VALUE || i > MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"expected an integer between %s and %s (inclusive). "
					+ "actual value is %s", 
					MIN_INT_VALUE,
					MAX_INT_VALUE,
					i));
		}
		return new UnsignedShort(i);
	}

	public static UnsignedShort newInstanceOf(final String s) {
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
		return newInstanceOf(i);
	}
	
	private final int intValue;
	
	private UnsignedShort(final int i) {
		this.intValue = i;
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
		UnsignedShort other = (UnsignedShort) obj;
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

	public byte[] toByteArray() {
		ByteBuffer bb = ByteBuffer.allocate(BYTE_ARRAY_LENGTH);
		bb.putShort((short) this.intValue);
		return bb.array();
	}
	
	@Override
	public String toString() {
		return Integer.toString(this.intValue);
	}	
}