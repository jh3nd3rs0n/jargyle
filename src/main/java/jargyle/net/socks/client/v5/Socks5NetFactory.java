package jargyle.net.socks.client.v5;

import jargyle.net.DatagramSocketInterfaceFactory;
import jargyle.net.HostnameResolverFactory;
import jargyle.net.NetFactory;
import jargyle.net.ServerSocketInterfaceFactory;
import jargyle.net.SocketInterfaceFactory;

public final class Socks5NetFactory extends NetFactory {

	private final Socks5Client socks5Client;
	
	public Socks5NetFactory(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public DatagramSocketInterfaceFactory newDatagramSocketInterfaceFactory() {
		return new Socks5DatagramSocketInterfaceFactory(this.socks5Client);
	}

	@Override
	public HostnameResolverFactory newHostnameResolverFactory() {
		return new Socks5HostnameResolverFactory(this.socks5Client);
	}

	@Override
	public ServerSocketInterfaceFactory newServerSocketInterfaceFactory() {
		return new Socks5ServerSocketInterfaceFactory(this.socks5Client);
	}

	@Override
	public SocketInterfaceFactory newSocketInterfaceFactory() {
		return new Socks5SocketInterfaceFactory(this.socks5Client);
	}

}
