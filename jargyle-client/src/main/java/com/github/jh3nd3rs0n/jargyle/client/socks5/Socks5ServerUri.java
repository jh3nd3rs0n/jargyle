package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SchemeConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(final String hst, final Integer prt) {
		super(SchemeConstants.SOCKS5, hst, prt);
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
