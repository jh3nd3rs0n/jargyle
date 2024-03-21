package com.github.jh3nd3rs0n.jargyle.protocolbase.internal;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class UnsignedShortIoHelper {

	/**
	 * The length of a byte array of an unsigned short.
	 */
	private static final int BYTE_ARRAY_LENGTH = 2;

	private UnsignedShortIoHelper() {
	}

	public static UnsignedShort readUnsignedShortFrom(
			final InputStream in) throws IOException {
		byte[] b = new byte[BYTE_ARRAY_LENGTH];
		int length = in.read(b);
		if (length != BYTE_ARRAY_LENGTH) {
			throw new EOFException("the end of the input stream is reached");
		}
		ByteBuffer bb = ByteBuffer.wrap(b);
		return UnsignedShort.valueOf(bb.getShort());
	}

	public static byte[] toByteArray(final UnsignedShort unsignedShort) {
		ByteBuffer bb = ByteBuffer.allocate(BYTE_ARRAY_LENGTH);
		bb.putShort(unsignedShort.shortValue());
		return bb.array();
	}

	public static UnsignedShort toUnsignedShort(final byte[] b) {
		UnsignedShort unsignedShort;
		try {
			unsignedShort = readUnsignedShortFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return unsignedShort;
	}

}
