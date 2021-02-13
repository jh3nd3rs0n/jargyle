package jargyle.net.socks.socks5.client;

import jargyle.net.HostnameResolver;
import jargyle.net.HostnameResolverFactory;

public final class Socks5HostnameResolverFactory 
	extends HostnameResolverFactory {

	private final Socks5Client socks5Client;
	
	public Socks5HostnameResolverFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public HostnameResolver newHostnameResolver() {
		return new Socks5HostnameResolver(this.socks5Client);
	}

}
