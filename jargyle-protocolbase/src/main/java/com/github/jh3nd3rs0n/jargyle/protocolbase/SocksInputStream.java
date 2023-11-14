package com.github.jh3nd3rs0n.jargyle.protocolbase;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedShort;

public class SocksInputStream extends FilterInputStream {

	protected SocksInputStream(final InputStream in) {
		super(in);
	}
	
	protected final UnsignedByte readUnsignedByte() throws IOException {
		int b = in.read();
		if (b == -1) {
			throw new EOFException("the end of the input stream is reached");
		}
		return UnsignedByte.newInstance(b);
	}
	
	protected final UnsignedShort readUnsignedShort() throws IOException {
		byte[] b = new byte[UnsignedShort.BYTE_ARRAY_LENGTH];
		int length = in.read(b);
		if (length != UnsignedShort.BYTE_ARRAY_LENGTH) {
			throw new IOException("the end of the input stream is reached");
		}
		return UnsignedShort.newInstance(b);
	}

}
