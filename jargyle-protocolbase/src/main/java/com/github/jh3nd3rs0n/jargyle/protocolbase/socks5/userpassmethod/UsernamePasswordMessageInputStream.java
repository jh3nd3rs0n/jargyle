package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import java.io.IOException;
import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5InputStream;

public class UsernamePasswordMessageInputStream extends Socks5InputStream {

	UsernamePasswordMessageInputStream(final InputStream in) {
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
