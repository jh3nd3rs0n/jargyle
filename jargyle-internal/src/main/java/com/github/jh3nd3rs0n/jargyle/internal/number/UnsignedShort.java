package com.github.jh3nd3rs0n.jargyle.internal.number;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class UnsignedShort {

	public static final int BYTE_ARRAY_LENGTH = 2;
	
	public static final int MAX_INT_VALUE = 0xffff;
	public static final int MIN_INT_VALUE = 0x0000;
	
	public static UnsignedShort newInstance(final byte[] b) {
		if (b.length != BYTE_ARRAY_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"expected a byte array of a length of %s. "
					+ "actual byte array length is %s", 
					BYTE_ARRAY_LENGTH,
					b.length));
		}
		ByteBuffer bb = ByteBuffer.wrap(new byte[] { b[0], b[1] });
		return newInstance(bb.getShort());
	}
	
	public static UnsignedShort newInstance(final int i) {
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
	
	public static UnsignedShort newInstance(final short s) {
		return newInstance(s & MAX_INT_VALUE);
	}
	
	public static UnsignedShort newInstance(final String s) {
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
	
	public static UnsignedShort newInstanceFrom(
			final InputStream in) throws IOException {
		UnsignedShort s = nullableFrom(in);
		if (s == null) {
			throw new IOException("the end of the input stream is reached");
		}
		return s;
	}
	
	public static UnsignedShort nullableFrom(
			final InputStream in) throws IOException {
		byte[] b = new byte[BYTE_ARRAY_LENGTH];
		int length = in.read(b);
		if (length != BYTE_ARRAY_LENGTH) {
			return null;
		}
		return newInstance(b);
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
	
	public short shortValue() {
		return (short) this.intValue;
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
