package com.github.jh3nd3rs0n.jargyle.protocolbase.internal;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

public final class UnsignedShortInputHelper {

	public static UnsignedShort readUnsignedShortFrom(
			final InputStream in) throws IOException {
		byte[] b = new byte[UnsignedShort.BYTE_ARRAY_LENGTH];
		int length = in.read(b);
		if (length != UnsignedShort.BYTE_ARRAY_LENGTH) {
			throw new IOException("the end of the input stream is reached");
		}
		return UnsignedShort.newInstance(b);
	}
	
	private UnsignedShortInputHelper() { }
	
}
