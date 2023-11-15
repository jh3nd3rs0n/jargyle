package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteInputHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

final class VersionInputHelper {
	
	public static Version readVersionFrom(
			final InputStream in) throws IOException {
		UnsignedByte b = UnsignedByteInputHelper.readUnsignedByteFrom(in);
		Version version = null;
		try {
			version = Version.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return version;
	}

	private VersionInputHelper() { }
	
}
