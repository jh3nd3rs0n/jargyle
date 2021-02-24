package jargyle.net.socks.client.v5;

import jargyle.net.HostResolver;
import jargyle.net.HostResolverFactory;

public final class Socks5HostResolverFactory 
	extends HostResolverFactory {

	private final Socks5Client socks5Client;
	
	public Socks5HostResolverFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public HostResolver newHostResolver() {
		return new Socks5HostResolver(this.socks5Client);
	}

}
