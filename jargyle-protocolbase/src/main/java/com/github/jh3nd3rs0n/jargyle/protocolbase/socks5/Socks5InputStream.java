package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.InputStream;

import com.github.jh3nd3rs0n.jargyle.protocolbase.SocksInputStream;

public class Socks5InputStream extends SocksInputStream {

	protected Socks5InputStream(final InputStream in) {
		super(in);
	}
	
}
