package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public class Socks5MessageInputStream extends Socks5InputStream {

	Socks5MessageInputStream(final InputStream in) {
		super(in);
	}
	
	final Version readVersion() throws IOException {
		UnsignedByte b = this.readUnsignedByte();
		Version version = null;
		try {
			version = Version.valueOfByte(b.byteValue());
		} catch (IllegalArgumentException e) {
			throw new Socks5Exception(e);
		}
		return version;
	}

}
