package com.github.jh3nd3rs0n.jargyle.protocolbase.internal;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class UnsignedByteIoHelper {
	
	public static UnsignedByte readUnsignedByteFrom(
			final InputStream in) throws IOException {
		int b = in.read();
		if (b == -1) {
			throw new EOFException("the end of the input stream is reached");
		}
		return UnsignedByte.valueOf(b);
	}
	
	private UnsignedByteIoHelper() { }

}
