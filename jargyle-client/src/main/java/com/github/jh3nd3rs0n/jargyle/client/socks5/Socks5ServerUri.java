package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(final String hst) {
		super(Scheme.SOCKS5, hst);
	}

	public Socks5ServerUri(final String hst, final int prt) {
		super(Scheme.SOCKS5, hst, prt);
	}

	public Socks5ServerUri(final Host hst, final Port prt) {
		super(Scheme.SOCKS5, hst, prt);
	}

	@Override
	public SocksClient newSocksClient(final Properties properties) {
		return new Socks5Client(this, properties);
	}

	@Override
	public SocksClient newSocksClient(
			final Properties properties, final SocksClient chainedSocksClient) {
		return new Socks5Client(this, properties, chainedSocksClient);
	}

}
