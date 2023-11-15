package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;

final class MethodInputHelper {
	
	public static Method readMethodFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		Method method = null;
		try {
			method = Method.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return method;		
	}
	
	private MethodInputHelper() { }
	
}
