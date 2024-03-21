package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.IOException;
import java.io.InputStream;

final class MethodIoHelper {
	
	public static Method readMethodFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
		Method method;
		try {
			method = Method.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return method;		
	}
	
	private MethodIoHelper() { }
	
}
