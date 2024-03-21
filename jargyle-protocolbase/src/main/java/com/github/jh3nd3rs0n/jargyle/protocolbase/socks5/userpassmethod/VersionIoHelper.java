package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

import java.io.IOException;
import java.io.InputStream;

final class VersionIoHelper {
	
	public static void readVersionFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
		try {
			Version.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
	}

	private VersionIoHelper() { }
	
}
