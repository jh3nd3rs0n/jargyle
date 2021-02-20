package jargyle.net.socks.client.v5;

import jargyle.net.DatagramSocketFactory;
import jargyle.net.HostnameResolverFactory;
import jargyle.net.NetFactory;
import jargyle.net.ServerSocketFactory;
import jargyle.net.SocketFactory;

public final class Socks5NetFactory extends NetFactory {

	private final Socks5Client socks5Client;
	
	public Socks5NetFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public DatagramSocketFactory newDatagramSocketFactory() {
		return new Socks5DatagramSocketFactory(this.socks5Client);
	}

	@Override
	public HostnameResolverFactory newHostnameResolverFactory() {
		return new Socks5HostnameResolverFactory(this.socks5Client);
	}

	@Override
	public ServerSocketFactory newServerSocketFactory() {
		return new Socks5ServerSocketFactory(this.socks5Client);
	}

	@Override
	public SocketFactory newSocketFactory() {
		return new Socks5SocketFactory(this.socks5Client);
	}

}
