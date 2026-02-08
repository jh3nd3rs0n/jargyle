package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.*;

public final class Socks5Client extends SocksClient {

	private final SocksDatagramSocketFactory socksDatagramSocketFactory;
	private final SocksHostResolverFactory socksHostResolverFactory;
	private final SocksServerSocketFactory socksServerSocketFactory;
	private final SocksSocketFactory socksSocketFactory;

	Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	Socks5Client(
			final Socks5ServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		super(serverUri, props, chainedClient);
		this.socksDatagramSocketFactory = new Socks5DatagramSocketFactory(this);
		this.socksHostResolverFactory = new Socks5HostResolverFactory(this);
		this.socksServerSocketFactory = new Socks5ServerSocketFactory(this);
		this.socksSocketFactory = new Socks5SocketFactory(this);
	}

	@Override
	public SocksDatagramSocketFactory getSocksDatagramSocketFactory() {
		return this.socksDatagramSocketFactory;
	}

	@Override
	public SocksHostResolverFactory getSocksHostResolverFactory() {
		return this.socksHostResolverFactory;
	}

	@Override
	public SocksServerSocketFactory getSocksServerSocketFactory() {
		return this.socksServerSocketFactory;
	}

	@Override
	public SocksSocketFactory getSocksSocketFactory() {
		return this.socksSocketFactory;
	}

}
